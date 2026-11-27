package ca.pkay.rcloneexplorer.RemoteConfig

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.Toast
import ca.pkay.rcloneexplorer.Activities.MainActivity
import ca.pkay.rcloneexplorer.R
import ca.pkay.rcloneexplorer.Rclone
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.ArrayList

class ConfigCreate internal constructor(
    options: ArrayList<String>?,
    private val mFormView: View,
    private val mAuthView: View,
    private val mContext: Context,
    private val mRclone: Rclone
) {
    private val options: ArrayList<String> = ArrayList(options ?: emptyList())
    private var job: Job? = null

    fun execute() {
        mAuthView.visibility = View.VISIBLE
        mFormView.visibility = View.GONE

        job = CoroutineScope(Dispatchers.Main).launch {
            val success = withContext(Dispatchers.IO) {
                OauthHelper.createOptionsWithOauth(options, mRclone, mContext)
            }
            if (!success) {
                Toasty.error(
                    mContext,
                    mContext.getString(R.string.error_creating_remote),
                    Toast.LENGTH_SHORT,
                    true
                ).show()
            } else {
                Toasty.success(
                    mContext,
                    mContext.getString(R.string.remote_creation_success),
                    Toast.LENGTH_SHORT,
                    true
                ).show()
            }
            val intent = Intent(mContext, MainActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            mContext.startActivity(intent)
        }
    }

    fun cancel() {
        job?.cancel()
    }
}
