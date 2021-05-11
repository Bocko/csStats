package hu.bme.aut.android.stats.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import com.google.android.material.tabs.TabLayoutMediator
import hu.bme.aut.android.stats.R
import hu.bme.aut.android.stats.databinding.ActivityDetailBinding
import hu.bme.aut.android.stats.detail.adapter.DetailPagerAdapter
import hu.bme.aut.android.stats.model.friends.FriendlistData
import hu.bme.aut.android.stats.model.profile.ProfileData
import hu.bme.aut.android.stats.model.stats.PlayerStatsData
import hu.bme.aut.android.stats.network.NetworkManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailActivity : AppCompatActivity(),PlayerDataHolder {

    lateinit var binding: ActivityDetailBinding

    companion object {
        private const val TAG = "DetailsActivity"
        const val PLAYER_NAME = "player_name"
    }

    private var playerID: Long? = null
    private var profileData: ProfileData? = null
    private var statsData: PlayerStatsData? = null
    private var friendlistData: FriendlistData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        playerID = intent.getStringExtra(PLAYER_NAME)?.toLong()
    }

    override fun onResume() {
        super.onResume()
        loadProfileData()
        loadFriendlistData()
        loadStatsData()

        val detailPagerAdapter = DetailPagerAdapter(this)
        binding.mainViewPager.adapter = detailPagerAdapter

        TabLayoutMediator(binding.tabLayout, binding.mainViewPager) { tab, position ->
            tab.text = when(position) {
                0 -> getString(R.string.profile)
                1 -> getString(R.string.friendlist)
                2 -> getString(R.string.stat)
                3 -> getString(R.string.chart)
                else -> ""
            }
        }.attach()

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun getProfileData(): ProfileData? = profileData
    override fun getStatsData(): PlayerStatsData? = statsData
    override fun getFriendlistData(): FriendlistData? = friendlistData

    private fun loadStatsData(){
        NetworkManager.getStats(playerID)!!.enqueue(object : Callback<PlayerStatsData?> {

            override fun onResponse(call: Call<PlayerStatsData?>,response: Response<PlayerStatsData?>) {

                Log.d(TAG, "stats onResponse: " + response.code())
                if (response.isSuccessful) {
                    displayStatsData(response.body())
                } else {
                    Toast.makeText(this@DetailActivity,"Private Profile" + response.message(),Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<PlayerStatsData?>,throwable: Throwable) {
                throwable.printStackTrace()
                Toast.makeText(this@DetailActivity,"Network request error occurred, check LOG",Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun displayStatsData(receivedStatData: PlayerStatsData?) {

        statsData = receivedStatData

        val detailPagerAdapter = DetailPagerAdapter(this)
        binding.mainViewPager.adapter = detailPagerAdapter
    }

    private fun loadProfileData(){
        NetworkManager.getProfile(playerID)!!.enqueue(object : Callback<ProfileData?> {

            override fun onResponse( call: Call<ProfileData?>, response: Response<ProfileData?>) {
                Log.d(TAG, "profile onResponse: " + response.code())
                if (response.isSuccessful) {
                    displayProfileData(response.body())
                } else {
                    Toast.makeText(this@DetailActivity, "Error: " + response.message(), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ProfileData?>, throwable: Throwable) {
                throwable.printStackTrace()
                Toast.makeText(this@DetailActivity, "Network request error occurred, check LOG", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun displayProfileData(receivedProfileData: ProfileData?) {
        profileData = receivedProfileData

        val detailPagerAdapter = DetailPagerAdapter(this)
        binding.mainViewPager.adapter = detailPagerAdapter
    }

    private fun loadFriendlistData(){
        NetworkManager.getFriends(playerID)!!.enqueue(object : Callback<FriendlistData?> {

            override fun onResponse(call: Call<FriendlistData?>,response: Response<FriendlistData?>) {

                Log.d(TAG, "Friends onResponse: " + response.code())
                if (response.isSuccessful) {
                    displayFriendsData(response.body())
                } else {
                    Toast.makeText(this@DetailActivity,"Private Friends" + response.message(),Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<FriendlistData?>,throwable: Throwable) {
                throwable.printStackTrace()
                Toast.makeText(this@DetailActivity,"Network request error occurred, check LOG",Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun displayFriendsData(receivedFriendData: FriendlistData?) {
        friendlistData = receivedFriendData

        val detailPagerAdapter = DetailPagerAdapter(this)
        binding.mainViewPager.adapter = detailPagerAdapter
    }
}