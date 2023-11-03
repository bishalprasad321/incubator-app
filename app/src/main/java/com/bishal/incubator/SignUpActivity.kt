package com.bishal.incubator

import android.app.Activity
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bishal.incubator.databinding.ActivitySignUpBinding
import com.bishal.incubator.models.User
import com.bishal.incubator.utils.USER_DEFAULT_BIO
import com.bishal.incubator.utils.USER_DEFAULT_PASSWORD
import com.bishal.incubator.utils.USER_NODE
import com.bishal.incubator.utils.USER_PROFILE_FOLDER
import com.bishal.incubator.utils.generateDefaultUserName
import com.bishal.incubator.utils.uploadImage
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.FacebookSdk
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

@Suppress("DEPRECATION")
class SignUpActivity : AppCompatActivity() {

    private lateinit  var auth: FirebaseAuth

    private lateinit var googleSignInClient: GoogleSignInClient

    private val binding: ActivitySignUpBinding by lazy {
        ActivitySignUpBinding.inflate(layoutInflater)
    }

    private lateinit var user: User
    private var userImage: String? = null

    // Gallery Launcher for adding profile image
    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let{
            uploadImage(uri, USER_PROFILE_FOLDER) {
                if (it == null) {
                    Toast.makeText(this@SignUpActivity, "Failed to load image", Toast.LENGTH_SHORT).show()
                } else {
                    userImage = it
                    binding.profileImageView.setImageURI(uri)
                }
            }
        }
    }

    // Intent launcher for google login
    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            binding.signUpProgressBar.visibility = View.VISIBLE
            if (task.isSuccessful) {
                val account: GoogleSignInAccount? = task.result
                val credentials = GoogleAuthProvider.getCredential(account?.idToken, null)
                auth.signInWithCredential(credentials).addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(this@SignUpActivity, "Success google sign in", Toast.LENGTH_LONG).show()
                        binding.nameEtView.setText(FirebaseAuth.getInstance().currentUser?.displayName)
                        binding.emailEtView.setText(FirebaseAuth.getInstance().currentUser?.email)
                        binding.profileImageView.setImageURI(FirebaseAuth.getInstance().currentUser?.photoUrl)
                        userImage = FirebaseAuth.getInstance().currentUser?.photoUrl.toString()
                        saveUserToDatabase(
                            binding.nameEtView.text.toString(),
                            binding.emailEtView.text.toString(),
                            userImage,
                            USER_DEFAULT_PASSWORD
                        )
                    } else {
                        Toast.makeText(this@SignUpActivity, it.exception?.localizedMessage, Toast.LENGTH_LONG).show()
                    }
                }
            }
        } else {
            binding.signUpProgressBar.visibility = View.INVISIBLE
            Toast.makeText(this@SignUpActivity, "Failed google sign in", Toast.LENGTH_LONG).show()
        }
    }

    private fun handleFacebookAccessToken(token: AccessToken) {
        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = auth.currentUser
                    updateUI(user!!)
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(
                        baseContext,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                    updateUI(null)
                }
            }
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            startActivity(Intent(this@SignUpActivity, HomeActivity::class.java))
            finish()
        } else {
            Toast.makeText(this@SignUpActivity, "Please Sign in to continue", Toast.LENGTH_LONG).show()
        }
    }

    // Initialize facebook login button
    private val mCallbackManager: CallbackManager = CallbackManager.Factory.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Facebook sdk initialization
        FacebookSdk.sdkInitialize(this@SignUpActivity)

        binding.facebookButton.setOnClickListener { binding.facebookLoginButton.performClick() }

        // facebook login button
        binding.facebookLoginButton.setReadPermissions("email", "public_profile")
        binding.facebookLoginButton.registerCallback(
            mCallbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult) {
                    handleFacebookAccessToken(result.accessToken)
                }

                override fun onCancel() {

                }

                override fun onError(error: FacebookException) {

                }
            },
        )

        // Google sign in options
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        // initialisation of lateinit variables
        auth = Firebase.auth
        user = User()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // Sign in text view (back to sign in activity)
        binding.askSignInTextView.setOnClickListener{
            startActivity(Intent(this@SignUpActivity, SignInActivity::class.java).addFlags(FLAG_ACTIVITY_REORDER_TO_FRONT))
        }

        // Back button
        binding.backButton.setOnClickListener {
            super.onBackPressed()
        }

        // Sign up button
        binding.signUpButton.setOnClickListener {
            createAccountWithEmail()
        }

        // Image icon button
        binding.addProfileImageIcon.setOnClickListener{
            galleryLauncher.launch("image/*")
        }

        // Google button
        binding.googleButton.setOnClickListener {
            binding.signUpProgressBar.visibility = View.VISIBLE
            val signInClient = googleSignInClient.signInIntent
            launcher.launch(signInClient)
        }
    }

    private fun createAccountWithEmail() {
        if ((binding.nameEtView.text.toString() == "") or
            (binding.emailEtView.text.toString() == "") or
            (binding.passwordEtView.editText?.text.toString() == "")
        ) {
            Toast.makeText(this@SignUpActivity, "Please fill all the text fields", Toast.LENGTH_SHORT).show()
        } else {

            binding.signUpProgressBar.visibility = View.VISIBLE

            auth.createUserWithEmailAndPassword(
                binding.emailEtView.text.toString(), binding.passwordEtView.editText?.text.toString()
            ).addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    saveUserToDatabase(
                        binding.nameEtView.text.toString(),
                        binding.emailEtView.text.toString(),
                        userImage,
                        binding.passwordEtView.editText?.text.toString())
                    Toast.makeText(this@SignUpActivity, "Account Created Successfully", Toast.LENGTH_SHORT).show()
                } else {
                    binding.signUpProgressBar.visibility = View.INVISIBLE
                    Toast.makeText(this@SignUpActivity, task.exception?.localizedMessage, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun saveUserToDatabase(name: String?, email: String?, image: String?, password: String?) {
        user.name = name
        user.lowercaseName = name?.lowercase()
        user.email = email
        user.image = image
        user.password = password
        user.bio = USER_DEFAULT_BIO
        user.username = generateDefaultUserName(email)

        Firebase.firestore.collection(USER_NODE).document(auth.currentUser!!.uid).set(user)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    binding.signUpProgressBar.visibility = View.GONE
                    Toast.makeText(this@SignUpActivity, "Sign up successful", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@SignUpActivity, HomeActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this@SignUpActivity, it.exception?.localizedMessage, Toast.LENGTH_LONG).show()
                }
            }
    }
}