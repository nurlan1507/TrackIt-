package com.nurlan1507.trackit.fragments

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.nurlan1507.trackit.MainActivity
import com.nurlan1507.trackit.R
import com.nurlan1507.trackit.databinding.FragmentLoginBinding
import com.nurlan1507.trackit.helpers.validateEmail
import com.nurlan1507.trackit.repositories.Failure
import com.nurlan1507.trackit.repositories.Success
import com.nurlan1507.trackit.viewmodels.UserViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await


class LoginFragment : Fragment() {
    private var mGoogleSignInClient: GoogleSignInClient? = null

    private lateinit var _binding:FragmentLoginBinding
    val binding get() = _binding
    private val userViewModel:UserViewModel by activityViewModels()
    private val auth = FirebaseAuth.getInstance()

    private lateinit var onTapClient: SignInClient

    private lateinit var gso:GoogleSignInOptions
    private lateinit var gsoClient:GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("JUSTDEBUG", "LLLLLL")

        super.onCreate(savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.hide()
        onTapClient =  Identity.getSignInClient(requireContext())
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().requestIdToken(getString(
            com.nurlan1507.trackit.R.string.default_web_client_id)).build()
        gsoClient =  GoogleSignIn.getClient(activity as AppCompatActivity,gso)

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater,container,false)
        _binding.apply {
            viewmodel = userViewModel
        }
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.loginBtn.setOnClickListener {
            if(activity?.currentFocus != null){
                val service = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                service.hideSoftInputFromWindow(view.windowToken,0)
            }
            if(!validateEmail(binding.inputEmail)) return@setOnClickListener
            if(binding.inputPassword.text.toString().isEmpty())return@setOnClickListener
            userViewModel.login(binding.inputEmail.text.toString(),binding.inputPassword.text.toString()){
                if(it is Failure){
                    binding.authErrorMessage?.text = it.error
                }else if(it is Success){
                    findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                    userViewModel.setUser(it.user)
                    userViewModel.setOwner()
                }
            }
        }

        binding.linkToRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        binding.loginGoogleBtn.setOnClickListener {
//            gsoClient.signOut()
//            googleSignIn()
                createRequest()
                signIn()
        }

        binding.resetPasswordLink.setOnClickListener {
            invokeDialog()
        }



    }



    private fun invokeDialog(){
        val view:View = layoutInflater.inflate(R.layout.reset_password_dialog,null)
        val input:TextInputEditText = view.findViewById(R.id.dialog_email_input)
        val builder = AlertDialog.Builder(requireContext())
        builder.setView(view)
        builder.setPositiveButton("Reset password") { dialog, which ->
            //Do nothing here because we override this button later to change the close behaviour.
            //However, we still need this because on older versions of Android unless we
            //pass a handler the button doesn't get instantiated
        }
              .setNegativeButton("Exit",null)
        val alertDialog:AlertDialog = builder.create()
        alertDialog.show()
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            var toClose = false
            if(input.text.toString().isEmpty()){
                input.error = "Provide yout email address"
                input.requestFocus()
            }else{
                toClose = true
            }
            if(toClose){
                FirebaseAuth.getInstance().sendPasswordResetEmail(input.text.toString())
                Toast.makeText(requireContext(), "We have sent you a link to email!", Toast.LENGTH_LONG).show()
                alertDialog.dismiss()
            }
        }
    }



    private fun handleSignInResult(task:Task<GoogleSignInAccount>) {
        try {

            val account = task.result
            val credentials = GoogleAuthProvider.getCredential(account.idToken, null)
            FirebaseAuth.getInstance().signInWithCredential(credentials).addOnCompleteListener {
                if(it.isSuccessful){
                    GlobalScope.launch{

                        userViewModel.googleOauth(it.result.user?.uid, account)
                        }
                    findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                }else{
                    Toast.makeText(requireContext(),it.exception?.message,Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: ApiException) {
            Toast.makeText(requireContext(),e.message, Toast.LENGTH_SHORT).show()
        }
    }
    private fun googleSignIn(){

        if(auth.currentUser!=null){
            gsoClient.signOut()
        }

        val signInIntent = gsoClient.signInIntent
        startActivityForResult(signInIntent,200)

//        launcher.launch(signInIntent)
    }

    private val launcher  = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        result ->
            Log.d("JUSTDEBUG", "LLLLLL")
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            handleSignInResult(task)
    }
    override fun onDestroy() {
        (activity as MainActivity).enableDrawer()
        (activity as AppCompatActivity).supportActionBar?.show()
        super.onDestroy()
    }


//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        try{
//
//            Log.d("AZOV", GoogleSignIn.getSignedInAccountFromIntent(data).result.email.toString() )
//
//            if(requestCode == 200){
//                Log.d("AZOV", GoogleSignIn.getSignedInAccountFromIntent(data).result.email.toString() )
//
//                GoogleSignIn.getSignedInAccountFromIntent(data).addOnCompleteListener {
//                    Log.d("AZOV", it.result.email.toString() )
//                    handleSignInResult(it.result)
//                }
//            }
//        }catch (e:Exception){
//
//        }
//        }



    private fun createRequest() {


        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(com.nurlan1507.trackit.R.string.default_web_client_id))
            .requestEmail()
            .build()


        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(activity as AppCompatActivity, gso)
        mGoogleSignInClient!!.signOut()
    }
    private fun signIn() {
        val signInIntent = mGoogleSignInClient!!.signInIntent
        startActivityForResult(signInIntent, 123)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == 123) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                GlobalScope.launch {
                    firebaseAuthWithGoogle(account)
                }
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                // ...
                Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private  suspend fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        try{
            var res = FirebaseAuth.getInstance().signInWithCredential(credential).await()
            userViewModel.googleOauth(res.user?.uid, acct)
            findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
        }catch (e:Exception){

        }


    }
}