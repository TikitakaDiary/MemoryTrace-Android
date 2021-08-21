package com.upf.memorytrace_android.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
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

        binding.btnGoogleLogin.setOnClickListener {
            loginWithGoogle()
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
                viewModel.register(
                    user.kakaoAccount?.profile?.nickname ?: "덕지",
                    user.id.toString(), KAKAO
                )
            }
        }
    }

    private fun loginWithGoogle() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        val googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        val signInIntent: Intent = googleSignInClient.signInIntent
        googleSignInLauncher.launch(signInIntent)
    }


    private val googleSignInLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                account?.idToken?.let {
                    setGoogleUserInfo(it)
                }
            } catch (e: ApiException) {
                Log.w(TAG, "Google sign in failed", e)
            }
        }

    private fun setGoogleUserInfo(idToken: String) {
        val auth = FirebaseAuth.getInstance()
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    //register user
                    viewModel.register(user.displayName, user.uid, GOOGLE)
                } else {
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                }
            }
    }


    companion object {
        private const val TAG = "LoginFragment"
        private const val KAKAO = "KAKAO"
        private const val GOOGLE = "GOOGLE"
    }
}
