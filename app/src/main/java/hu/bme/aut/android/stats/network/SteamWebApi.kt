package hu.bme.aut.android.stats.network

import hu.bme.aut.android.stats.model.friends.FriendlistData
import hu.bme.aut.android.stats.model.playercount.CountData
import hu.bme.aut.android.stats.model.profile.ProfileData
import hu.bme.aut.android.stats.model.stats.PlayerStatsData
import hu.bme.aut.android.stats.model.url.UrlData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface SteamWebApi {
    @GET("/ISteamUser/ResolveVanityURL/v1/")
    fun getID(
            @Query("key") key: String?,
            @Query("vanityurl") vanityURL: String?
    ): Call<UrlData?>?

    @GET("/ISteamUserStats/GetUserStatsForGame/v2/")
    fun getStats(
            @Query("key") key: String?,
            @Query("steamid") steamid: Long?,
            @Query("appid") appId: Long?
    ): Call<PlayerStatsData?>?

    @GET("/ISteamUser/GetPlayerSummaries/v2/")
    fun getProfile(
            @Query("key") key: String?,
            @Query("steamids") steamids: Long?
    ): Call<ProfileData?>?

    @GET("/ISteamUser/GetPlayerSummaries/v2/")
    fun getFriendsProfiles(
            @Query("key") key: String?,
            @Query("steamids") steamids: String?
    ): Call<ProfileData?>?

    @GET("/ISteamUserStats/GetNumberOfCurrentPlayers/v1/")
    fun getPlayerCount(
            @Query("appid") appid: Int?
    ): Call<CountData?>?

    @GET("/ISteamUser/GetFriendList/v1/")
    fun getFriends(
            @Query("key") key: String?,
            @Query("steamid") steamid: Long?
    ): Call<FriendlistData?>?

    @GET("/ISteamUser/GetPlayerBans/v1/")
    fun getBans(
            @Query("key") key: String?,
            @Query("steamids") steamids: Long?
    ): Call<FriendlistData?>?
}