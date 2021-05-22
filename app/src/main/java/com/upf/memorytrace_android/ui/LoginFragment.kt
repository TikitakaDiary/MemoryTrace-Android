package com.upf.memorytrace_android.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import com.upf.memorytrace_android.R
import com.upf.memorytrace_android.base.BaseFragment
import com.upf.memorytrace_android.databinding.FragmentLoginBinding
import com.upf.memorytrace_android.viewmodel.LoginViewModel


internal class LoginFragment : BaseFragment<LoginViewModel, FragmentLoginBinding>() {
    override val layoutId = R.layout.fragment_login
    override val viewModelClass = LoginViewModel::class

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initViews()
    }

    private fun initViews() {
        binding.btnKakaoLogin.setOnClickListener {
            loginWithKakao()
        }
    }

    private fun loginWithKakao() {
        // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(requireContext())) {
            UserApiClient.instance.loginWithKakaoTalk(
                requireContext(),
                callback = kakaoSignInCallback
            )
        } else {
            UserApiClient.instance.loginWithKakaoAccount(
                requireContext(),
                callback = kakaoSignInCallback
            )
        }
    }

    private val kakaoSignInCallback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            Log.e(TAG, "로그인 실패", error)
        } else if (token != null) {
            Log.i(TAG, "로그인 성공 ${token.accessToken}")
            setKakaoUserInfo(token.accessToken)
        }
    }

    private fun setKakaoUserInfo(token: String) {
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e("LoginFragment", "사용자 정보 요청 실패", error)
            } else if (user != null) {
                //register user
                //TODO: 토큰 관리 어떻게 되는지 추가 확인 필요, 이미 가입된 유저면 로그인처리를 타야함.(현재는 생성만 있음)
                viewModel.register(
                    user.kakaoAccount?.profile?.nickname ?: "덕지",
                    token, KAKAO
                )
            }
        }
    }

    companion object {
        private const val TAG = "LoginFragment"
        private const val KAKAO = "KAKAO"
        private const val GOOGLE = "GOOGLE"
    }
}
