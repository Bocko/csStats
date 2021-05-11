package hu.bme.aut.android.stats.detail.fragment.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import hu.bme.aut.android.stats.databinding.ItemFriendBinding
import hu.bme.aut.android.stats.databinding.ItemPlayerBinding
import hu.bme.aut.android.stats.menu.adapter.MenuAdapter
import hu.bme.aut.android.stats.model.profile.Player
import hu.bme.aut.android.stats.model.profile.ProfileData
import hu.bme.aut.android.stats.model.url.UrlData
import hu.bme.aut.android.stats.network.NetworkManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class FriendAdapter (private val listener: OnPlayerSelectedListener) : RecyclerView.Adapter<FriendAdapter.FriendViewHolder>() {

    private val TAG = "FriendActivity"
    private var friends: MutableList<Player?> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = FriendViewHolder(
            ItemFriendBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: FriendViewHolder, position: Int) {
        val item = friends[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = friends.size

    fun addFriend(ID: String) {
        if(ID.isNotEmpty()){
            loadFriendsProfilesData(ID)
        }
    }

    inner class FriendViewHolder(val binding: ItemFriendBinding) :
            RecyclerView.ViewHolder(binding.root) {
        var item: Player? = null
        /*
        init {
            binding.root.setOnClickListener {
                listener.onPlayerSelected(item?.steamid)
            }
        }*/

        fun bind(newFriend: Player?) {
            item = newFriend!!
            binding.tvFriendName.text = item?.personaname
            Glide.with(binding.root)
                    .load(item?.avatarfull)
                    .transition(DrawableTransitionOptions().crossFade())
                    .into(binding.ivFriendImg)
        }
    }

    interface OnPlayerSelectedListener {
        fun onPlayerSelected(player: Long?)
    }

    private fun loadFriendsProfilesData(playerIDorURL: String?){
        NetworkManager.getFriendsProfiles(playerIDorURL)!!.enqueue(object : Callback<ProfileData?> {

            override fun onResponse(call: Call<ProfileData?>, response: Response<ProfileData?>) {
                Log.d(TAG, "Friends profiles onResponse: " + response.code())
                if (response.isSuccessful) {
                    displayProfileData(response.body())
                } else {
                    Log.d(TAG, "Error: " + response.message())
                }
            }

            override fun onFailure(call: Call<ProfileData?>, throwable: Throwable) {
                throwable.printStackTrace()
                Log.d(TAG, "Network request error occurred")
            }
        })
    }

    private fun displayProfileData(receivedProfileData: ProfileData?) {
        receivedProfileData?.response?.players?.forEach {
            friends.add(it)
            notifyItemInserted(friends.size - 1)
        }
    }

}