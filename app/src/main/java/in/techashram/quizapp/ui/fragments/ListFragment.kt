package `in`.techashram.quizapp.ui.fragments

import `in`.techashram.quizapp.R
import `in`.techashram.quizapp.ui.adapter.QuizListAdapter
import `in`.techashram.quizapp.viewmodel.QuizListViewModel
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_list.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ListFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var viewModel: QuizListViewModel
    lateinit var quizListAdapter : QuizListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        quizListAdapter = QuizListAdapter()
        rvQuiz.apply {
            adapter = quizListAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        quizListAdapter.onItemClicked {
            // get item clicked
            // go to details
            val action = ListFragmentDirections.actionListFragmentToDetailsFragment(it)
            findNavController().navigate(action)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(QuizListViewModel::class.java)

        viewModel.quizListLiveData.observe(viewLifecycleOwner, Observer {
            quizListAdapter.differ.submitList(it)
        })
    }
}