package com.persia.test.ui.panel.profile

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.persia.test.R
import com.persia.test.databinding.FragmentProfileBinding
import timber.log.Timber
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Color.WHITE
import com.canhub.cropper.CropImage
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageView
import com.canhub.cropper.options
import android.os.Environment
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    companion object {

        fun newInstance() = ProfileFragment()

        const val DATE_FORMAT = "yyyyMMdd_HHmmss"
        const val FILE_NAMING_PREFIX = "JPEG_"
        const val FILE_NAMING_SUFFIX = "_"
        const val FILE_FORMAT = ".jpg"
        const val AUTHORITY_SUFFIX = ".cropper.fileprovider"
    }

    private val profileViewModel: ProfileViewModel by viewModels()

    private lateinit var _binding: FragmentProfileBinding
    private val binding get() = _binding

    private var outputUri: Uri? = null
    private val takePicture = registerForActivityResult(ActivityResultContracts.TakePicture()) {
        if (it) startCameraWithUri() else showErrorMessage("taking picture failed")
    }
    private val cropImage = registerForActivityResult(CropImageContract()) { result ->
        when {
            result.isSuccessful -> {
                Timber.v("Bitmap: ${result.bitmap.toString()}")
                Timber.v("File Path: ${context?.let { result.getUriFilePath(it) }.toString()}")
                handleCropImageResult(result)
            }
            result is CropImage.CancelledResult -> {
                showErrorMessage("cropping image was cancelled by the user")
            }
            else -> {
                showErrorMessage("cropping image failed")
            }
        }
    }
    private val customCropImage = registerForActivityResult(CropImageContract()) {
        if (it is CropImage.CancelledResult) {
            return@registerForActivityResult
        }
        handleCropImageResult(it)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
        _binding.lifecycleOwner = viewLifecycleOwner
        _binding.profileViewModel = profileViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
        setupObservers()
        profileViewModel.refreshUserProfile()
        // binding.goToIncomeListBtn.setOnClickListener {
        //     profileViewModel.displayIncomes()
        // }
        //
        // profileViewModel.navigateToIncomeList.observe(viewLifecycleOwner) {
        //     if (it == true) {
        //         this.findNavController().navigate(R.id.incomeListFragment)
        //         Timber.i("clicked")
        //         profileViewModel.displayIncomesCompleted()
        //     }
        // }

    }

    private fun setupListeners() {
        binding.profileSelectAvatarImage.setOnClickListener {
            startCameraWithoutUri(includeCamera = false, includeGallery = true)
        }

        binding.profileFirstNameET.doOnTextChanged { text, start, before, count ->
            profileViewModel.onEvent(
                ProfileEditFormEvent.FirstnameChanged(firstname = text.toString())
            )
        }

        binding.profileLastNameET.doOnTextChanged { text, start, before, count ->
            profileViewModel.onEvent(
                ProfileEditFormEvent.LastnameChanged(lastname = text.toString())
            )
        }

        binding.submitProfileInfo.setOnClickListener {
            profileViewModel.onEvent(ProfileEditFormEvent.Submit)
        }
    }

    private fun setupObservers() {
        profileViewModel.apiError.observe(viewLifecycleOwner) { err ->
            err?.let {
                Toast.makeText(activity, "api error: $err", Toast.LENGTH_LONG).show()
            }
        }

        profileViewModel.userProfile.observe(viewLifecycleOwner) { data ->
            data?.let {
                profileViewModel.updateFormState()
            }
        }

        profileViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.submitProfileInfo.isEnabled = !isLoading
        }
    }

    private fun showErrorMessage(message: String) {
        Timber.e("Camera Error: $message")
        Toast.makeText(activity, "Crop failed: $message", Toast.LENGTH_SHORT).show()
    }

    private fun handleCropImageResult(result: CropImageView.CropResult) {
        val uri = result.uriContent.toString()
        Timber.i("result uir: $uri")
        Glide.with(binding.profileAvatarImageView.context)
            .load(uri)
            .fitCenter()
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.loading_animation)
                    .error(R.drawable.ic_broken_image)
            )
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(binding.profileAvatarImageView)
        // SampleResultScreen.start(this, null, Uri.parse(uri.replace("file:", "")), null)
        profileViewModel.uploadAvatarImage(result.getUriFilePath(context!!)!!)
    }

    private fun setupOutputUri() {
        if (outputUri == null) context?.let { ctx ->
            val authorities = "${ctx.applicationContext?.packageName}$AUTHORITY_SUFFIX"
            outputUri = FileProvider.getUriForFile(ctx, authorities, createImageFile())
        }
    }

    private fun createImageFile(): File {
        val timeStamp = SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).format(Date())
        val storageDir: File? = activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "$FILE_NAMING_PREFIX${timeStamp}$FILE_NAMING_SUFFIX",
            FILE_FORMAT,
            storageDir
        )
    }

    private fun startCameraWithoutUri(includeCamera: Boolean, includeGallery: Boolean) {
        customCropImage.launch(
            options {
                setImageSource(
                    includeGallery = includeGallery,
                    includeCamera = includeCamera,
                )
                // Normal Settings
                setScaleType(CropImageView.ScaleType.FIT_CENTER)
                setCropShape(CropImageView.CropShape.RECTANGLE)
                setGuidelines(CropImageView.Guidelines.ON_TOUCH)
                setAspectRatio(1, 1)
                setMaxZoom(4)
                setAutoZoomEnabled(true)
                setMultiTouchEnabled(true)
                setCenterMoveEnabled(true)
                setShowCropOverlay(true)
                setAllowFlipping(true)
                setSnapRadius(3f)
                setTouchRadius(48f)
                setInitialCropWindowPaddingRatio(0.1f)
                setBorderLineThickness(3f)
                setBorderLineColor(Color.argb(170, 255, 255, 255))
                setBorderCornerThickness(2f)
                setBorderCornerOffset(5f)
                setBorderCornerLength(14f)
                setBorderCornerColor(WHITE)
                setGuidelinesThickness(1f)
                setGuidelinesColor(R.color.white)
                setBackgroundColor(Color.argb(119, 0, 0, 0))
                setMinCropWindowSize(24, 24)
                setMinCropResultSize(20, 20)
                setMaxCropResultSize(99999, 99999)
                setActivityTitle("")
                setActivityMenuIconColor(0)
                setOutputCompressFormat(Bitmap.CompressFormat.JPEG)
                setOutputCompressQuality(90)
                setRequestedSize(0, 0)
                setRequestedSize(0, 0, CropImageView.RequestSizeOptions.RESIZE_INSIDE)
                setInitialCropWindowRectangle(null)
                setInitialRotation(0)
                setAllowCounterRotation(false)
                setFlipHorizontally(false)
                setFlipVertically(false)
                setCropMenuCropButtonTitle(null)
                setCropMenuCropButtonIcon(0)
                setAllowRotation(true)
                setNoOutputImage(false)
                setFixAspectRatio(true)
                // Odd Settings
//                setScaleType(CropImageView.ScaleType.CENTER)
//                setCropShape(CropImageView.CropShape.OVAL)
//                setGuidelines(CropImageView.Guidelines.ON)
//                setAspectRatio(4, 16)
//                setMaxZoom(8)
//                setAutoZoomEnabled(false)
//                setMultiTouchEnabled(false)
//                setCenterMoveEnabled(true)
//                setShowCropOverlay(false)
//                setAllowFlipping(false)
//                setSnapRadius(10f)
//                setTouchRadius(30f)
//                setInitialCropWindowPaddingRatio(0.3f)
//                setBorderLineThickness(5f)
//                setBorderLineColor(R.color.black)
//                setBorderCornerThickness(6f)
//                setBorderCornerOffset(2f)
//                setBorderCornerLength(20f)
//                setBorderCornerColor(RED)
//                setGuidelinesThickness(5f)
//                setGuidelinesColor(RED)
//                setBackgroundColor(Color.argb(119, 30, 60, 90))
//                setMinCropWindowSize(20, 20)
//                setMinCropResultSize(16, 16)
//                setMaxCropResultSize(999, 999)
//                setActivityTitle("CUSTOM title")
//                setActivityMenuIconColor(RED)
//                setActivityMenuTextColor(Color.BLACK)
//                setOutputUri(outputUri)
//                setOutputCompressFormat(Bitmap.CompressFormat.PNG)
//                setOutputCompressQuality(50)
//                setRequestedSize(100, 100)
//                setRequestedSize(100, 100, CropImageView.RequestSizeOptions.RESIZE_FIT)
//                setInitialCropWindowRectangle(null)
//                setInitialRotation(180)
//                setAllowCounterRotation(true)
//                setFlipHorizontally(true)
//                setFlipVertically(true)
//                setCropMenuCropButtonTitle("Custom name")
//                setCropMenuCropButtonIcon(R.drawable.ic_gear_24)
//                setAllowRotation(false)
//                setNoOutputImage(false)
//                setFixAspectRatio(true)
//                setSkipEditing(true)
//                setShowIntentChooser(true)
//                setIntentChooserTitle("My Intent Chooser")
/*                setIntentChooserPriorityList(listOf(
                    "com.miui.gallery",
                    "com.google.android.apps.photos"
                  ))
*/
//                setActivityBackgroundColor(Color.BLACK)
//                setToolbarColor(Color.WHITE)
//                setToolbarTitleColor(Color.BLACK)
//                setToolbarBackButtonColor(Color.BLACK)
//                setToolbarTintColor(Color.BLACK)
            }
        )
    }

    private fun startCameraWithUri() {
        cropImage.launch(
            options(outputUri) {
                setScaleType(CropImageView.ScaleType.FIT_CENTER)
                setCropShape(CropImageView.CropShape.RECTANGLE)
                setGuidelines(CropImageView.Guidelines.ON_TOUCH)
                setAspectRatio(1, 1)
                setMaxZoom(4)
                setAutoZoomEnabled(true)
                setMultiTouchEnabled(true)
                setCenterMoveEnabled(true)
                setShowCropOverlay(true)
                setAllowFlipping(true)
                setSnapRadius(3f)
                setTouchRadius(48f)
                setInitialCropWindowPaddingRatio(0.1f)
                setBorderLineThickness(3f)
                setBorderLineColor(Color.argb(170, 255, 255, 255))
                setBorderCornerThickness(2f)
                setBorderCornerOffset(5f)
                setBorderCornerLength(14f)
                setBorderCornerColor(WHITE)
                setGuidelinesThickness(1f)
                setGuidelinesColor(R.color.white)
                setBackgroundColor(Color.argb(119, 0, 0, 0))
                setMinCropWindowSize(24, 24)
                setMinCropResultSize(20, 20)
                setMaxCropResultSize(99999, 99999)
                setActivityTitle("")
                setActivityMenuIconColor(0)
                setOutputUri(null)
                setOutputCompressFormat(Bitmap.CompressFormat.JPEG)
                setOutputCompressQuality(90)
                setRequestedSize(0, 0)
                setRequestedSize(0, 0, CropImageView.RequestSizeOptions.RESIZE_INSIDE)
                setInitialCropWindowRectangle(null)
                setInitialRotation(0)
                setAllowCounterRotation(false)
                setFlipHorizontally(false)
                setFlipVertically(false)
                setCropMenuCropButtonTitle(null)
                setCropMenuCropButtonIcon(0)
                setAllowRotation(true)
                setNoOutputImage(false)
                setFixAspectRatio(false)
            }
        )
    }
}