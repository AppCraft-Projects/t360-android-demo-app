package com.example.demodatingapp.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.demodatingapp.R
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class RegistrationFragment: Fragment() {
    companion object {
        const val SIGN_IN_REQUEST_CODE = 1102
        val FACEBOOK_PERMISSIONS = listOf("email", "public_profile")
    }

    val callbackManager = CallbackManager.Factory.create()
    val facebookLoginManager = LoginManager.getInstance()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<com.example.demodatingapp.databinding.FragmentRegisterBinding>(inflater, R.layout.fragment_register, container, false)
        binding.facebookLoginButton.setOnClickListener {
            facebookLoginManager.logInWithReadPermissions(this, FACEBOOK_PERMISSIONS)
        }
        binding.googleSignInButton.setOnClickListener {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build()
            val client = GoogleSignIn.getClient(context!!, gso)
            startActivityForResult(client.signInIntent, SIGN_IN_REQUEST_CODE)
        }
        facebookLoginManager.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult?) {
                handleFacebookAccessToken(result!!.accessToken)
            }

            override fun onCancel() {
            }

            override fun onError(error: FacebookException?) {
                Toast.makeText(context, error!!.localizedMessage, Toast.LENGTH_LONG).show()
            }

        })
        return binding.root
    }

    private fun handleFacebookAccessToken(accessToken: AccessToken) {
        val credential = FacebookAuthProvider.getCredential(accessToken.token)
        FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {
                findNavController().navigate(R.id.fragment_list)
            } else {
                Toast.makeText(context, it.exception!!.localizedMessage, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == SIGN_IN_REQUEST_CODE) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                handleGoogleSignin(account)
            } catch (e: ApiException) {

            }
        }
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)

    }

    private fun handleGoogleSignin(account: GoogleSignInAccount?) {
        val credential =  GoogleAuthProvider.getCredential(account!!.idToken, null)
        FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {
                findNavController().navigate(R.id.fragment_list)
            } else {
                Toast.makeText(context, it.exception!!.localizedMessage, Toast.LENGTH_LONG).show()
            }
        }
    }
}