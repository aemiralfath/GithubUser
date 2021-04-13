package com.aemiralfath.githubuser.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.aemiralfath.githubuser.databinding.FragmentFollowBinding
import com.aemiralfath.githubuser.model.entity.FollowResponse
import com.aemiralfath.githubuser.view.activity.DetailUserActivity
import com.aemiralfath.githubuser.view.adapter.UserFollowAdapter
import com.aemiralfath.githubuser.viewmodel.DetailUserViewModel


class FollowFragment : Fragment() {

    private lateinit var adapter: UserFollowAdapter
    private lateinit var binding: FragmentFollowBinding
    private lateinit var detailUserViewModel: DetailUserViewModel

    companion object {

        private const val ARG_USERNAME = "username"
        private const val ARG_SECTION = "section"

        @JvmStatic
        fun newInstance(
            username: String,
            section: Int
        ) =
            FollowFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_USERNAME, username)
                    putInt(ARG_SECTION, section)
                }
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFollowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val username = arguments?.getString(ARG_USERNAME)
        val section = arguments?.getInt(ARG_SECTION, 0)

        adapter = UserFollowAdapter()
        adapter.notifyDataSetChanged()

        binding.rvUsersFollow.setHasFixedSize(true)
        binding.rvUsersFollow.layoutManager = LinearLayoutManager(activity)
        binding.rvUsersFollow.adapter = adapter

        detailUserViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(DetailUserViewModel::class.java)

        showLoading(true)
        if (section == 0) {
            username?.let { detailUserViewModel.setUserFollowers(requireContext(), it) }
            detailUserViewModel.getDataUserFollowers().observe(viewLifecycleOwner, getFollow)
        } else {
            username?.let { detailUserViewModel.setUserFollowing(requireContext(), it) }
            detailUserViewModel.getDataUserFollowing().observe(viewLifecycleOwner, getFollow)
        }

        adapter.setOnItemClickCallback(object : UserFollowAdapter.OnItemClickCallback {
            override fun onItemClicked(data: FollowResponse) {
                val intent = Intent(context, DetailUserActivity::class.java)
                intent.putExtra(DetailUserActivity.EXTRA_USER, data.login)
                startActivity(intent)
            }
        })
    }

    private val getFollow: Observer<List<FollowResponse>> =
        Observer<List<FollowResponse>> {
            adapter.listUsersFollow = it as ArrayList<FollowResponse>
            adapter.notifyDataSetChanged()
            showLoading(false)
        }

    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }


}