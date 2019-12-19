package com.sincerity.sinceutils.ui

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.Gravity
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.loader.app.LoaderManager
import androidx.loader.app.LoaderManager.LoaderCallbacks
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sincerity.framelibrary.defalut.DefaultNavigationBar
import com.sincerity.framelibrary.util.StateBarUtil
import com.sincerity.sinceutils.R
import com.sincerity.sinceutils.adapter.SelectImageAdapter
import com.sincerity.sinceutils.bean.SelectImageBean
import com.sincerity.sinceutils.common.ImageConstant
import com.sincerity.sinceutils.common.ImageConstant.LOADER_TYPE
import com.sincerity.sinceutils.common.ImageConstant.MODE_MULTI
import com.sincerity.sinceutils.image.itf.SelectImageListener
import com.sincerity.utilslibrary.base.BaseActivity
import com.sincerity.utilslibrary.view.RecycleView.decoration.GridLayoutDecoration
import java.io.File

/**
 * 图片选择器
 */

class PicSelectActivity : BaseActivity(), SelectImageListener, View.OnClickListener {
    //单选还是多选
    private var mMode = MODE_MULTI
    //是否显示拍照按钮
    private var isShowPhoneView = true
    //最多选择图片数量
    private var mMaxCount = 9
    //已经选择好的图片
    private var mResultList = ArrayList<String>()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SelectImageAdapter
    private lateinit var mTvPreview: TextView
    private lateinit var mTvMaxCount: TextView
    private lateinit var mTvDetermine: TextView
    private var mTempFile: String = ""
    override fun setContentView() = R.layout.activity_pic_select

    override fun initViews() {
        recyclerView = findViewById(R.id.id_img_container)
        recyclerView.layoutManager = GridLayoutManager(this, 4)
        recyclerView.addItemDecoration(GridLayoutDecoration(this, 0))
        mTvPreview = findViewById(R.id.id_text_preview)
        mTvMaxCount = findViewById(R.id.id_text_count)
        mTvDetermine = findViewById(R.id.id_text_determine)

    }

    override fun initListener() {
        mTvDetermine.setOnClickListener(this)
    }

    /**
     * 框架思想去处理
     */
    override fun initData() {
        val intent = intent
        mMaxCount = intent.getIntExtra(ImageConstant.EXTRA_SELECT_COUNT, mMaxCount)
        mMode = intent.getIntExtra(ImageConstant.EXTRA_SELECT_MODE, MODE_MULTI)
        isShowPhoneView = intent.getBooleanExtra(ImageConstant.EXTRA_SHOW_CAMERA, isShowPhoneView)
        mResultList = intent.getStringArrayListExtra(ImageConstant.EXTRA_DEFAULT_SELECTED_LIST)!!
        initImageList()
        exchangeViewShow()
    }

    //改变布局的显示
    private fun exchangeViewShow() {
        if (mResultList.size > 0) {
            //至少选择一张
            mTvPreview.isEnabled = true
            mTvPreview.setOnClickListener {
                val toast = Toast.makeText(this, "预览图", Toast.LENGTH_SHORT)
                toast.setGravity(Gravity.CENTER, 0, 0)
                toast.show()
            }
        } else {
            //一张没有
            mTvPreview.isEnabled = false
        }
        mTvMaxCount.text = ("${mResultList.size} / $mMaxCount")
    }

    private fun initImageList() {
        //初始化本地图片  还是操作 需要操作游标
        LoaderManager.getInstance(this).initLoader(LOADER_TYPE, null, mLoadCallBack)
    }

    private val mLoadCallBack = object : LoaderCallbacks<Cursor> {
        private val IMAGE_PROJECTICON = arrayOf(
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.DATE_ADDED,
                MediaStore.Images.Media.MIME_TYPE,
                MediaStore.Images.Media.SIZE,
                MediaStore.Images.Media._ID)

        override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
            //查询数据 返回一个游标
            return CursorLoader(this@PicSelectActivity,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTICON
                    , "${IMAGE_PROJECTICON[4]} > 0 AND  ${IMAGE_PROJECTICON[3]} =? OR ${IMAGE_PROJECTICON[3]}=?",
                    arrayOf("image/jpeg", "image/png"),
                    "${IMAGE_PROJECTICON[2]} DESC")
        }

        override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
            //解析 封装到集合
            if (data != null && data.count > 0) {
                val list = ArrayList<SelectImageBean>()
                //添加拍照的占位符
                if (isShowPhoneView) {
                    val imageBean = SelectImageBean("", "拍照", 0L)
                    list.add(imageBean)
                }
                while (data.moveToNext()) {
                    val path = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTICON[0]))
                    val name = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTICON[1]))
                    val dateTime = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTICON[2]))
                    if (!File(path).exists()) {
                        continue
                    }
                    val imageBean = SelectImageBean(path, name, dateTime)
                    list.add(imageBean)
                }
                showImageListData(list)
            }

        }

        override fun onLoaderReset(loader: Loader<Cursor>) {

        }

    }

    //显示图片列表
    private fun showImageListData(list: ArrayList<SelectImageBean>) {
        adapter = SelectImageAdapter(this, list, mResultList, mMaxCount)
        recyclerView.adapter = adapter
        adapter.mListener = this
        adapter.mCameraListener = this

    }

    override fun initActionBar() {
        DefaultNavigationBar.Builder(this)
                .setRightText("搜索")
                .setRightOnClickListener(View.OnClickListener {
                    val toast = Toast.makeText(this, "搜索", Toast.LENGTH_SHORT)
                    toast.setGravity(Gravity.CENTER, 0, 0)
                    toast.show()
                }).setBackGroundColor(ContextCompat.getColor(applicationContext, R.color.img_bottom_color)).builder()
        StateBarUtil.stateBarTintColor(this, ContextCompat.getColor(this, R.color.bg_color))
    }

    override fun selectImage(position: Int) {
        if (position == 0) {
            val fileDir = File(Environment.getExternalStorageDirectory(), "Pictures")
            if (!fileDir.exists()) {
                fileDir.mkdir()
            }
            val fileName = "/IMG_${System.currentTimeMillis()}.jpg"
            mTempFile = fileDir.absolutePath + File.separator + fileName
            openCamera(mTempFile)
        }else{
            exchangeViewShow()
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.id_text_determine -> {
                startForResultActivity()
            }
        }

    }

    private fun openCamera(fileName: String) {
        var uri: Uri? = null
        //		设置保存参数到ContentValues中
        val contentValues = ContentValues()
        //设置文件名
        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
        //兼容Android Q和以下版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            //android Q中不再使用DATA字段，而用RELATIVE_PATH代替 RELATIVE_PATH是相对路径不是绝对路径
            //DCIM是系统文件夹，关于系统文件夹可以到系统自带的文件管理器中查看，不可以写没存在的名字
            contentValues.put(MediaStore.Images.Media.RELATIVE_PATH, "DCIM/Pictures")
        } else {
            //Android Q以下版本
            val picPath = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            //图片名称
            val mFileName = "IMG_${System.currentTimeMillis()}.jpg"
            //图片路径
            val mFilePath = picPath?.absolutePath + File.separator + mFileName
            contentValues.put(MediaStore.Images.Media.DATA, mFilePath)
        }
        //设置文件类型
        contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/JPEG")
        //执行insert操作，向系统文件夹中添加文件
        //EXTERNAL_CONTENT_URI代表外部存储器，该值不变
        uri = this.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        // 若生成了uri，则表示该文件添加成功
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        startActivityForResult(intent, ImageConstant.CAMARE)
    }

    private fun startForResultActivity() {
        val intent = Intent()
        intent.putStringArrayListExtra(ImageConstant.EXTRA_DEFAULT_SELECTED_LIST, mResultList)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //1.把图片添加到集合
        //2.调用sureSelect
        //3.
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == ImageConstant.CAMARE ) {
                val resolver = contentResolver
                val uri = data?.data
                var mBitmap: Bitmap? = null
                if (uri != null) {
                    mBitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        val source = ImageDecoder.createSource(resolver, uri)
                        ImageDecoder.decodeBitmap(source)
                    } else {
                        sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://$mTempFile")))
                        MediaStore.Images.Media.getBitmap(resolver, uri)
                    }
                }
                mResultList.add(mTempFile)
                exchangeViewShow()
            }
        }
    }

    override fun onRestart() {
        super.onRestart()
        LoaderManager.getInstance(this).restartLoader(LOADER_TYPE, null, mLoadCallBack)
    }

    override fun onDestroy() {
        super.onDestroy()
        LoaderManager.getInstance(this).destroyLoader(LOADER_TYPE)
    }

    override fun onPause() {
        super.onPause()
        LoaderManager.getInstance(this).restartLoader(LOADER_TYPE, null, mLoadCallBack)
    }
}
