package com.creativeduck.mrdaebak.data

import retrofit2.Call
import retrofit2.http.*

interface RemoteService {
    // 회원가입
    @POST("user/register")
    fun register(@Body requestDto: RegisterRequestDto): Call<Int>

    // 로그인
    @POST("user/login")
    fun login(@Body requestDto: LoginRequestDto): Call<LoginResponseDto>

    // 회원탈퇴
    @DELETE("user/{userId}")
    fun withdrawal(@Path("userId") mrUserId: Long): Call<Int>

    // 정보수정
    @PATCH("user/{userId}")
    fun reviseUser(@Path("userId") mrUserId: Long, @Body requestDto: UserDto): Call<UserDto>

    // 정보 가져오기
    @GET("user/{userId}")
    fun getUser(@Path("userId") mrUserId: Long): Call<UserDto>

    // 주문 추가
    @POST("order/{userId}")
    fun order(@Path("userId") mrUserId: Long, @Body requestDto: OrderDto): Call<Int>

    // 주문 내역 조회
    @GET("order/{userId}")
    fun getOrder(@Path("userId") mrUserId: Long): Call<OrderListDto>

    // 주문 상세 조회
    @GET("order/{userId}/{orderId}")
    fun getOrderDetail(
        @Path("userId") mrUserId: Long,
        @Path("orderId") orderId: Long
    ): Call<OrderDto>

    // 주문 취소, 조리 완료, 배달 완료
    @PATCH("order/{orderId}/{orderState}")
    fun updateOrder(
        @Path("orderId") orderId: Long,
        @Path("orderState") orderState: String
    ): Call<Int>

    // 식자재 조회
    @GET("ingredient")
    fun getIngredient(): Call<IngredientListModel>

    // 식자재 개수 변경
    @PATCH("ingredient")
    fun updateIngredient(@Body requestDto: IngredientListModel): Call<Int>

    // 조리 목록 조회
    @GET("cook")
    fun getCooking(): Call<OrderListDto>

    // 요리사의 조리 목록 조회
    @GET("cook/{cookId}")
    fun getCookingForCook(@Path("cookId") cookId: Long): Call<OrderListDto>

    // 요리 시작
    @POST("cook/{cookId}/{orderId}")
    fun startCook(@Path("cookId") cookId: Long, @Path("orderId") orderId: Long): Call<Int>

    // 배달 목록 조회
    @GET("delivery")
    fun getDelivery(): Call<OrderListDto>

    // 배달원의 배달 목록 조회
    @GET("delivery/{riderId}")
    fun getDeliveryForRider(@Path("riderId") riderId: Long): Call<OrderListDto>

    // 배달 시작
    @POST("delivery/{riderId}/{orderId}")
    fun startDelivery(@Path("riderId") riderId: Long, @Path("orderId") orderId: Long): Call<Int>
}