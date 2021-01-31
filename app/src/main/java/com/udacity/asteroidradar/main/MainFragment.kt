package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding


class MainFragment : Fragment() {


    private val viewModel: MainViewModel by lazy {
        val activity = requireNotNull(this.activity)
        ViewModelProvider(
            this,
            MainViewModel.Factory(activity.application)
        ).get(MainViewModel::class.java)
    }

    private var viewModelAdapter: AsteroidsAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this

        viewModel.asteroids.observe(viewLifecycleOwner, Observer { asteroids ->
            asteroids?.apply {
                viewModelAdapter?.asteroids = asteroids
            }
        })

        viewModelAdapter = AsteroidsAdapter(AsteroidClick {
            viewModel.displayAsteroidDetails(it)
        })



        viewModel.navigateToSelectedAsteroid.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                this.findNavController().navigate(
                    MainFragmentDirections.actionShowDetail(it)
                )
                viewModel.displayAsteroidDetailsComplete()
            }
        })
        binding.asteroidRecycler.adapter = viewModelAdapter

        binding.viewModel = viewModel


        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        viewModel.setFilter(
            when (item.itemId) {
                R.id.show_week_menu -> AsteroidApiFilter.SHOW_WEEK
                R.id.show_today_menu -> AsteroidApiFilter.SHOW_TODAY
                else -> AsteroidApiFilter.SHOW_ALL
            }
        )
        return true
    }
}
