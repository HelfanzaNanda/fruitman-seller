package com.one.fruitmanseller.ui.product

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import coil.api.load
import com.fxn.pix.Options
import com.fxn.pix.Pix
import com.one.fruitmanseller.R
import com.one.fruitmanseller.models.Product
import com.one.fruitmanseller.utils.Constants
import com.one.fruitmanseller.utils.extensions.showToast
import kotlinx.android.synthetic.main.activity_product.*
import kotlinx.android.synthetic.main.content_product.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

class ProductActivity : AppCompatActivity() {

    private val IMAGE_REQ_CODE = 101
    private val productViewModel: ProductViewModel by viewModel()
    private var imageUrl = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)
        setSupportActionBar(toolbar)
        setUpToolbar()
        observer()
        fill()
        pickImage()
    }

    private fun pickImage (){
        btn_add_image.setOnClickListener {
            val opt = Options.init()
                .setRequestCode(IMAGE_REQ_CODE)
                .setCount(1)
                .setExcludeVideos(true)
            Pix.start(this@ProductActivity, opt)
        }
    }

    private fun observer() {
        productViewModel.listenToState().observer(this, Observer { handleUiState(it) })
    }

    private fun fill(){
        if (isInsert()){
            btn_submit.text = "insert"
            btn_submit.setOnClickListener {
                val token = Constants.getToken(this@ProductActivity)
                val name = et_name.text.toString().trim()
                val price = et_price.text.toString().trim()
                val desc = et_description.text.toString().trim()
                val address = et_address.text.toString().trim()
                if (productViewModel.validate(name, price, desc,  address, imageUrl)){
                    val productToSend = Product(name = name, price = price.toInt(), address = address, description = desc)
                    productViewModel.createProduct(token, productToSend, imageUrl)
                }else{
                    showInfoAlert("Not valid")
                }
            }
        }else{
            btn_submit.isEnabled = true
            btn_submit.text = "update"
            getProduct()
            btn_submit.setOnClickListener {
                val token = Constants.getToken(this@ProductActivity)
                val name = et_name.text.toString().trim()
                val price = et_price.text.toString().trim()
                val desc = et_description.text.toString().trim()
                val address = et_address.text.toString().trim()
                if (productViewModel.validate(name, price, desc,  address, null)){
                    val productToSend = Product(name = name, price = price.toInt(), address = address, description = desc)
                    productViewModel.updateProduct(token, getPassedProduct()?.id.toString(), productToSend, imageUrl)
                }else{
                    showInfoAlert("Not valid")
                }
            }
        }
    }

    private fun setUpToolbar (){
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
        toolbar.setNavigationOnClickListener { finish() }
        supportActionBar?.title = if(isInsert()) resources.getString(R.string.add_product) else resources.getString(R.string.edit_product)
    }
    private fun handleUiState(it : ProductState){
        when(it){
            is ProductState.ShowToast -> showToast(it.message)
            is ProductState.IsLoading -> handleLoading(it.state)
            is ProductState.SuccessCreate -> handleSuccessCreate()
            is ProductState.SuccessUpdate -> handleSuccessUpdate()
            is ProductState.SuccessDelete -> handleSuccessDelete()
            is ProductState.Reset -> handleReset()
            is ProductState.Validate -> handleValidate(it)
        }
    }

    private fun handleSuccessDelete() {
        finish()
        showToast("berhasil menghapus produk")
    }

    private fun handleSuccessCreate() {
        finish()
        showToast("berhasil menambahkan produk")
    }

    private fun handleSuccessUpdate() {
        finish()
        showToast("berhasil mengupdate produk")
    }

    private fun handleValidate(validate: ProductState.Validate) {
        validate.name?.let { setNameErr(it) }
        validate.price?.let { setPriceErr(it) }
        validate.address?.let { setAddressErr(it) }
        validate.desc?.let { setDescErr(it) }
        validate.image?.let { setImageErr(it) }
    }

    private fun handleReset() {
        setNameErr(null)
        setPriceErr(null)
        setAddressErr(null)
        setDescErr(null)
    }

    private fun handleLoading(state: Boolean) {
        btn_submit.isEnabled = !state
    }

    private fun getProduct(){
        getPassedProduct()?.let {
            et_name.setText(it.name.toString())
            et_price.setText((it.price.toString()))
            et_description.setText(it.description.toString())
            et_address.setText(it.address.toString())
            iv_product.load(it.image)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == IMAGE_REQ_CODE && resultCode == Activity.RESULT_OK && data != null){
            val selectedImageUri = data.getStringArrayListExtra(Pix.IMAGE_RESULTS)
            selectedImageUri?.let {
                iv_product.load(File(it[0]))
                imageUrl = it[0]
                btn_submit.isEnabled = true
            }
        }
    }

    private fun showInfoAlert(message: String){
        AlertDialog.Builder(this).apply {
            setMessage(message)
            setPositiveButton("ya"){ d, _ -> d.dismiss()}
        }.show()
    }

    private fun deleteProduct(){
        getPassedProduct()?.let { product ->
            AlertDialog.Builder(this).apply {
                setMessage(resources.getString(R.string.ask_delete))
                setPositiveButton(resources.getString(R.string.delete)){ d, _ ->
                    productViewModel.deleteProduct(Constants.getToken(this@ProductActivity), product.id.toString())
                    d.dismiss()
                }
                setNegativeButton(resources.getString(R.string.cancel)){ d, _ -> d.cancel() }
            }.show()
        }
    }

    private fun isInsert() = intent.getBooleanExtra("IS_INSERT", true)
    private fun getPassedProduct() : Product? = intent.getParcelableExtra("PRODUCT")
    private fun setNameErr(err : String?) { til_name.error = err }
    private fun setPriceErr(err : String?) { til_price.error = err }
    private fun setAddressErr(err : String?) { til_address.error = err }
    private fun setDescErr(err : String?) { til_description.error = err }
    private fun setImageErr(err : String?) { showInfoAlert(err.toString()) }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.action_delete -> {
                deleteProduct()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        getPassedProduct()?.let{
            menuInflater.inflate(R.menu.menu_product, menu)
            return true
        }
        return true
    }
}