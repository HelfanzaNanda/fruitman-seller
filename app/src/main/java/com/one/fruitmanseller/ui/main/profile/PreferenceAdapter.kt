package com.one.fruitmanseller.ui.main.profile

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.one.fruitmanseller.R
import com.one.fruitmanseller.models.Preference
import com.one.fruitmanseller.ui.login.LoginActivity
import com.one.fruitmanseller.ui.update_password.UpdatePasswordActivity
import com.one.fruitmanseller.ui.update_profile.UpdateProfilActivity
import com.one.fruitmanseller.utils.Constants
import kotlinx.android.synthetic.main.list_item_preference.view.*

class PreferenceAdapter (private var context: Context, private var prefs : List<Preference>)
    : RecyclerView.Adapter<PreferenceAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_preference, parent, false))

    override fun getItemCount() = prefs.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(prefs[position], context, position)

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        fun bind(pref : Preference, context: Context, i : Int){
            with(itemView){
                pref_image.load(pref.image)
                pref_name.text = context.getString(pref.name)
                setOnClickListener {
                    when(i) {
                        0 -> context.startActivity(Intent(context, UpdateProfilActivity::class.java))
                        1 -> context.startActivity(Intent(context, UpdatePasswordActivity::class.java))
                        2 -> {
                            AlertDialog.Builder(context).apply {
                                setMessage("apakah yakin ingin logout?")
                                setPositiveButton("ya") { d, _ ->
                                    d.dismiss()
                                    Constants.clearToken(context)
                                    context.startActivity(Intent(context, LoginActivity::class.java))
                                    context as AppCompatActivity
                                    context.finish()
                                }
                                setNegativeButton("tidak") { d, _ -> d.cancel() }
                            }.show()
                        }
                        else -> println("Ya")
                    }
                }
            }
        }
    }
}