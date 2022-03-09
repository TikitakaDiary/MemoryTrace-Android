package com.upf.memorytrace_android.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import com.upf.memorytrace_android.R
import com.upf.memorytrace_android.ui.base.BaseFragment
import com.upf.memorytrace_android.databinding.FragmentLoginBinding
import com.upf.memorytrace_android.extension.setOnDebounceClickListener
import com.upf.memorytrace_android.ui.diary.detail.presentation.DetailFragmentDirections
import com.upf.memorytrace_android.util.showDialog


internal class LoginFragment : BaseFragment<LoginViewModel, FragmentLoginBinding>() {
    override val layoutId = R.layout.fragment_login
    override val viewModelClass = LoginViewModel::class

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initViews()
    }

    private fun initViews() {
        binding.btnKakaoLogin.setOnDebounceClickListener {
            showAgreeDialog { loginWithKakao() }
        }

        binding.btnGoogleLogin.setOnDebounceClickListener {
            showAgreeDialog { loginWithGoogle() }
        }

        binding.logoDuckz.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToTermsAgreeFragment(null))
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
                setGoogleUserInfo(account)
            } catch (e: ApiException) {
                Log.w(TAG, "Google sign in failed", e)
            }
        }

    private fun setGoogleUserInfo(account: GoogleSignInAccount?) {
        val auth = FirebaseAuth.getInstance()
        val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser ?: return@addOnCompleteListener
                    //register user
                    viewModel.register(
                        user.displayName ?: "",
                        account?.id ?: return@addOnCompleteListener,
                        GOOGLE,
                        user.uid
                    )
                } else {
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                }
            }
    }

    private fun showAgreeDialog(
        onAgree: () -> Unit
    ) {
        context?.let {
            showDialog(
                it,
                "개인정보 수집/이용 약관 동의",
                "계속 하시면 개인정보 수집 및 이용 약관에 동의하는 것으로 간주합니다.\n로그인 하시겠습니까?",
                "확인",
                onAgree
            )
        }
    }


    companion object {
        private const val TAG = "LoginFragment"
        private const val KAKAO = "KAKAO"
        private const val GOOGLE = "GOOGLE"
    }
}
