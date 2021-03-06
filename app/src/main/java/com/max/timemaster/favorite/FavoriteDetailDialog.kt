package com.max.timemaster.favorite

import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.max.timemaster.*
import com.max.timemaster.databinding.DialogFavoriteDetailBinding
import com.max.timemaster.ext.getVmFactory
import com.max.timemaster.util.SetColorStateList
import com.max.timemaster.util.UserManager

class FavoriteDetailDialog : AppCompatDialogFragment() {

    private val viewModel by viewModels<FavoriteDetailDialogViewModel> {
        getVmFactory()
    }
    lateinit var binding: DialogFavoriteDetailBinding
    lateinit var chipGroup: ChipGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.PublishDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)

        binding = DialogFavoriteDetailBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        binding.layoutPublish.startAnimation(
            AnimationUtils.loadAnimation(context, R.anim.anim_slide_up)
        )
        chipGroup = binding.chipGroup
        binding.addChip.setOnClickListener {

            val chip = Chip(chipGroup.context)
            val content = viewModel.edContent.value
            val listContent = viewModel.edListContent
            if (content != null) {
                viewModel.edListContent.add(content)
                for (i in listContent.indices) {
                    val textContent = listContent[i]

                    chip.setOnClickListener {
                        chip.isCloseIconEnabled = !chip.isCloseIconEnabled
                        //Added click listener on close icon to remove tag from ChipGroup
                        chip.setOnCloseIconClickListener {
                            viewModel.edListContent.remove(textContent)
                            chipGroup.removeView(chip)
                        }
                    }
                }
                chipSetUp(chip)
                chipGroup.addView(chip)
                viewModel.cleanContent()
            }
        }

        binding.buttonPublish.setOnClickListener {

            if (!viewModel.edTitle.value.isNullOrEmpty() && !viewModel.edListContent.isNullOrEmpty()) {
                viewModel.uploadDateFavorite()
            } else {
                findNavController().navigate(
                    NavigationDirections.navigateToMessengerDialog(
                        MessageType.INCOMPLETE_TEXT.value
                    )
                )
            }
        }

        mainViewModel.selectAttendee.observe(viewLifecycleOwner, Observer { selectDate ->

            viewModel.edAttendee.value = selectDate

            binding.niceSpinner.attachDataSource(viewModel.getSelectAttendeeFavorite())
            binding.niceSpinner.setOnItemSelectedListener(object :
                AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {}
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    viewModel.getSelectAttendeeFavorite()?.let {
                        viewModel.edTitle.value = it[position]
                    }
                }
            })
        })

        viewModel.leave.observe(viewLifecycleOwner, Observer {
            it?.let {

                findNavController().navigateUp()
                viewModel.onLeft()
            }
        })
        return binding.root
    }

    private fun chipSetUp(chip: Chip) {

        val dateColor = UserManager.myDate.value?.filter {
            it.name == viewModel.edAttendee.value
        }!![0].color

        val paddingDp = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            10f,
            TimeMasterApplication.instance.resources.displayMetrics
        ).toInt()

        chip.chipBackgroundColor = dateColor?.let { color ->
            SetColorStateList.setColorStateList(color)
        }

        chip.setPadding(40, paddingDp, paddingDp, paddingDp)
        chip.text = viewModel.edContent.value
        chip.setTextColor(Color.BLACK)
    }

}
