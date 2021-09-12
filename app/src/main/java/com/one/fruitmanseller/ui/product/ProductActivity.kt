package com.one.fruitmanseller.ui.product

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import com.fxn.pix.Options
import com.fxn.pix.Pix
import com.one.fruitmanseller.R
import com.one.fruitmanseller.models.Fruit
import com.one.fruitmanseller.models.Product
import com.one.fruitmanseller.models.SubDistrict
import com.one.fruitmanseller.ui.maps.Coordinate
import com.one.fruitmanseller.ui.maps.MapsActivity
import com.one.fruitmanseller.utils.Constants
import com.one.fruitmanseller.utils.extensions.gone
import com.one.fruitmanseller.utils.extensions.showToast
import com.one.fruitmanseller.utils.extensions.visible
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.SliderView
import kotlinx.android.synthetic.main.activity_product.*
import kotlinx.android.synthetic.main.content_product.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.ArrayList

class ProductActivity : AppCompatActivity() {

    companion object{
        private const val IMAGE_REQ_CODE = 101
        private const val IMAGE_REG_UPDATE = 102
        private const val RESULT_MAPS = 69
    }
    private val productViewModel: ProductViewModel by viewModel()
    private var imageUrl = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)
        setSupportActionBar(toolbar)
        setUpToolbar()
        setUpImageSlider1()
        setUpImageSlider2()
        if(!isInsert()) fetchProduct()
        observer()
        fill()
        goToMapsActivity()
        fetchFruits()
        fetchSubdistricts()
    }

    private fun setUpImageSlider1() {
        image_slider.apply {
            setSliderAdapter(ImgSliderAdapter(mutableListOf()))
            setIndicatorAnimation(IndicatorAnimationType.WORM)
            setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
            autoCycleDirection = SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH
            indicatorSelectedColor = Color.WHITE
            indicatorUnselectedColor = Color.GRAY
            scrollTimeInSec = 4
        }.startAutoCycle()
    }

    private fun setUpImageSlider2() {
        image_slider2.apply {
            setSliderAdapter(ImageSliderAdapter(mutableListOf()))
            setIndicatorAnimation(IndicatorAnimationType.WORM)
            setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
            autoCycleDirection = SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH
            indicatorSelectedColor = Color.WHITE
            indicatorUnselectedColor = Color.GRAY
            scrollTimeInSec = 4
        }.startAutoCycle()
    }

    private fun pickImage(ImageCode : Int){
        btn_add_image.setOnClickListener {
            val opt = Options.init()
                .setRequestCode(ImageCode)
                .setCount(5)
                .setExcludeVideos(true)
            Pix.start(this@ProductActivity, opt)
        }
    }

    private fun observer() {
        observeState()
        observeFruits()
        observeSubDistricts()
        if (!isInsert()) observeProduct()
    }

    private fun observeState() = productViewModel.listenToState().observer(this, Observer { handleUiState(it) })
    private fun observeSubDistricts() = productViewModel.listenToSubDistricts().observe(this, Observer { handleSubDistricts(it) })
    private fun observeFruits() = productViewModel.listenToFruits().observe(this, Observer { handleFruits(it) })
    private fun observeProduct() = productViewModel.listenToProduct().observe(this, Observer { handleProduct(it) })

    private fun handleProduct(product: Product?) {
        product?.let {
            et_price.setText((it.price.toString()))
            et_description.setText(it.description.toString())
            et_address.setText(it.address.toString())

            image_slider2.sliderAdapter?.let { pagerAdapter ->
                if (pagerAdapter is ImageSliderAdapter){
                    pagerAdapter.changelist(it.images)
                }
            }
        }
    }

    private fun getPathImages() = productViewModel.listenToPathImages().value

    private fun setIdSubDistrict(subDIstrictId : String) = productViewModel.setIdSubDistrict(subDIstrictId)
    private fun setIdFruit(fruitId : String) = productViewModel.setIdFruit(fruitId)
    private fun getIdSubDIstrict() = productViewModel.getIdSubDistrict().value
    private fun getIdFruit() = productViewModel.getIdFruit().value
    private fun fetchFruits() = productViewModel.fetchFruits(Constants.getToken(this@ProductActivity))
    private fun fetchSubdistricts() = productViewModel.fetchSubdistricts(Constants.getToken(this@ProductActivity))

    private fun findIndex(arr: Array<Int>, item: Int): Int? {
        return (arr.indices).firstOrNull { i: Int -> item == arr[i] };
    }
    private fun handleSubDistricts(list: List<SubDistrict>?) {
        list?.let { lisSubDistrict ->
            val subDistrictNames = mutableListOf<String>()
            lisSubDistrict.map { subDistrictNames.add(it.name!!) }
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, subDistrictNames)
                .apply {
                    setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                }
            spinner_sub_district.adapter = adapter
            if (!isInsert()){
                val f = lisSubDistrict.find { subDistrict -> subDistrict.id == getPassedProduct()?.subdistrict_id?.toInt() }
                val index = subDistrictNames.indexOf(f?.name)
                spinner_sub_district.setSelection(index)
            }
            spinner_sub_district.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {}

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    val filterDistrict = lisSubDistrict.find { subDistrict -> subDistrict.name == subDistrictNames[position] }
                    setIdSubDistrict(filterDistrict!!.id.toString())
                }
            }
        }
    }

    private fun handleFruits(listFruit: List<Fruit>) {
        val distinctFruit = listFruit.distinctBy { fruit -> fruit.name  }
        val fruitNames = mutableListOf<String>()
        distinctFruit.map { fruitNames.add(it.name!!) }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, fruitNames)
            .apply {
                setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            }
        spinner_fruit.adapter = adapter
        if (!isInsert()){
            val f = distinctFruit.find { fruit -> fruit.id == getPassedProduct()?.fruit_id?.toInt() }
            val index = fruitNames.indexOf(f?.name)
            spinner_fruit.setSelection(index)
        }
        spinner_fruit.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val fruit = listFruit.find { fruit ->fruit.name == fruitNames[position]  }
                setIdFruit(fruit!!.id.toString())
            }
        }
    }

    private fun fill(){
        if (isInsert()){
            pickImage(IMAGE_REQ_CODE)
            card_slider.visible()
            card_slider2.gone()
            btn_add_image.text = "tambah gambar"
            btn_submit.text = "tambah"
            btn_submit.setOnClickListener {
                val token = Constants.getToken(this@ProductActivity)
                val price = et_price.text.toString().trim()
                val desc = et_description.text.toString().trim()
                val address = et_address.text.toString().trim()
                val lat = productViewModel.getLat().value
                val lng = productViewModel.getLng().value
                getPathImages()?.let { listPathImage ->
                    if (productViewModel.validate( price, desc,  address, null)){
                        val productToSend = Product( price = price.toInt(),
                            address = address, description = desc, lat = lat, lng = lng,
                            subdistrict_id = getIdSubDIstrict(), fruit_id = getIdFruit())
                        //startWork()
                        productViewModel.createProduct(token, productToSend)
                    }else{
                        showInfoAlert("Not valid")
                    }
                }?:kotlin.run {
                    showInfoAlert("harus ada foto minimal 1, maksimal 5")
                }
            }
        }else{
            pickImage(IMAGE_REG_UPDATE)
            card_slider.gone()
            card_slider2.visible()
            btn_add_image.text = "edit gambar"
            btn_submit.isEnabled = true
            btn_submit.text = "ubah"
            //getProduct()
            btn_submit.setOnClickListener {
                val token = Constants.getToken(this@ProductActivity)
                val price = et_price.text.toString().trim()
                val desc = et_description.text.toString().trim()
                val address = et_address.text.toString().trim()
                val lat = productViewModel.getLat().value
                val lng = productViewModel.getLng().value
                if (productViewModel.validate( price, desc,  address, null)){
                    val productToSend = Product( price = price.toInt(),
                        address = address, description = desc, lat = lat, lng = lng,
                        subdistrict_id = getIdSubDIstrict(), fruit_id = getIdFruit())
                    productViewModel.updateProduct(token, getPassedProduct()?.id.toString(), productToSend)
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
            is ProductState.SuccessUpdatePhoto -> handleSuccessUpdatePhoto()
            is ProductState.Reset -> handleReset()
            is ProductState.Validate -> handleValidate(it)
        }
    }

    private fun handleSuccessUpdatePhoto() {
        showToast("berhasil update photo")
        fetchProduct()
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
        validate.price?.let { setPriceErr(it) }
        validate.address?.let { setAddressErr(it) }
        validate.desc?.let { setDescErr(it) }
        validate.image?.let { setImageErr(it) }
    }

    private fun handleReset() {
        setPriceErr(null)
        setAddressErr(null)
        setDescErr(null)
    }

    private fun handleLoading(state: Boolean) {
        btn_submit.isEnabled = !state
        if (state) loading.visible() else loading.gone()
    }

    private fun tokenUser() = Constants.getToken(this@ProductActivity)
    private fun updateImagesProduct(urlImages: ArrayList<String>) {
        productViewModel.updatePhotoProduct(tokenUser(), getPassedProduct()?.id.toString(), urlImages)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == IMAGE_REQ_CODE && resultCode == Activity.RESULT_OK && data != null) {
            val selectedImageUri = data.getStringArrayListExtra(Pix.IMAGE_RESULTS)
            selectedImageUri?.let { images ->
                var index = 0
                images.forEach { image ->
                    productViewModel.addImage(index.toString(), image)
                    index++
                }

                image_slider.sliderAdapter?.let { pagerAdapter ->
                    if (pagerAdapter is ImgSliderAdapter) {
                        pagerAdapter.changelist(images)
                    }
                }
                btn_submit.isEnabled = true
            }
        }

        if(requestCode == IMAGE_REG_UPDATE && resultCode == Activity.RESULT_OK && data != null){
            val returnedImages = data.getStringArrayListExtra(Pix.IMAGE_RESULTS)
            returnedImages?.let { updateImagesProduct(it) }
        }

        if(requestCode == RESULT_MAPS && resultCode == Activity.RESULT_OK && data != null) onLatLngReturned(data)


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
    private fun getPassedCoordinate(data: Intent) : Coordinate? = data.getParcelableExtra("RESULT_COORDINATE")
    private fun setPriceErr(err : String?) { til_price.error = err }
    private fun setAddressErr(err : String?) { til_address.error = err }
    private fun setDescErr(err : String?) { til_description.error = err }
    private fun setImageErr(err : String?) { showInfoAlert(err.toString()) }
    private fun onLatLngReturned(data: Intent) = productViewModel.setLatLng(getPassedCoordinate(data)!!.lat!!, getPassedCoordinate(data)!!.lng!!)

    private fun goToMapsActivity() {
        btn_to_maps.setOnClickListener {
            startActivityForResult(Intent(this@ProductActivity, MapsActivity::class.java), RESULT_MAPS)
        }
    }

    private fun fetchProduct() = productViewModel.findProduct(Constants.getToken(this@ProductActivity), getPassedProduct()?.id.toString())

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