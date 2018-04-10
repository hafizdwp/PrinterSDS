package com.example.printersds

import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.example.printersds.testprintclass.Dropping
import com.example.printersds.testprintclass.Tagihan
import kotlinx.android.synthetic.main.activity_main.*
import java.io.UnsupportedEncodingException

class MainActivity : AppCompatActivity() {

    companion object {
        // Message types sent from the BluetoothService Handler
        val MESSAGE_STATE_CHANGE = 1
        val MESSAGE_READ = 2
        val MESSAGE_WRITE = 3
        val MESSAGE_DEVICE_NAME = 4
        val MESSAGE_TOAST = 5
        val MESSAGE_CONNECTION_LOST = 6
        val MESSAGE_UNABLE_CONNECT = 7

        // Key names received from the BluetoothService Handler
        val DEVICE_NAME = "device_name"
        val TOAST = "toast"
    }

    /**********************************************************************************************/

    // Debugging
    private val TAG = "Main_Activity"
    private var DEBUG = true

    // Intent request codes
    private val REQUEST_CONNECT_DEVICE = 1
    private val REQUEST_ENABLE_BT = 2
    private val REQUEST_CHOSE_BMP = 3
    private val REQUEST_CAMER = 4

    // Local Bluetooth adapter
    private var mBluetoothAdapter: BluetoothAdapter? = null
    // Member object for the services
    private var mService: BluetoothService? = null
    // Name of the connected device
    private var mConnectedDeviceName: String? = null

    @SuppressLint("HandlerLeak")
    private val mHandler = object : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                MESSAGE_STATE_CHANGE -> if (DEBUG)
                    Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1)
                MESSAGE_WRITE -> {
                }
                MESSAGE_READ -> {
                }
                MESSAGE_DEVICE_NAME -> {
                    // save the connected device's name
                    mConnectedDeviceName = msg.data.getString(DEVICE_NAME)
                    Toast.makeText(applicationContext,
                            "Connected to " + mConnectedDeviceName!!,
                            Toast.LENGTH_SHORT).show()
                }
                MESSAGE_TOAST -> Toast.makeText(applicationContext,
                        msg.data.getString(TOAST), Toast.LENGTH_SHORT)
                        .show()
                MESSAGE_CONNECTION_LOST    //蓝牙已断开连接
                -> Toast.makeText(applicationContext, "Device connection was lost",
                        Toast.LENGTH_SHORT).show()
                MESSAGE_UNABLE_CONNECT     //无法连接设备
                -> Toast.makeText(applicationContext, "Unable to connect device",
                        Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**********************************************************************************************/

    // TESTING
    val FORMAT_LEFT = "left"
    val FORMAT_CENTER = "center"
    val FORMAT_RIGHT = "right"
    val STYLE_BOLD = "bold"
    val STYLE_SMALL = "small"

    val MAX_CHAR = 32
    val MAX_KIRI = 19 //19,20
    val MAX_TENGAH = 3
    val MAX_KANAN = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (DEBUG)
            Log.e(TAG, "+++ ON CREATE+++")

        // Get local Bluetooth adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

        // If the adapter is null, then Bluetooth is not supported
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available",
                    Toast.LENGTH_LONG).show()
            finish()
        }

        //button click listener
        btn_scan_device.setOnClickListener {
            val serverIntent = Intent(this@MainActivity, DeviceListActivity::class.java)
            startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE)
        }

        btn_print_dropping.setOnClickListener {
            Dropping(this).print()
        }

        btn_print_tagihan.setOnClickListener {
            Tagihan(this).print()
        }
    }

    override fun onStart() {
        super.onStart()

        // If Bluetooth is not on, request that it be enabled.
        // setupChat() will then be called during onActivityResult
        if (!mBluetoothAdapter!!.isEnabled) {
            val enableIntent = Intent(
                    BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT)
            // Otherwise, setup the session
        } else {
            if (mService == null)
                keyListenerInit()//监听
        }
    }

    @Synchronized public override fun onResume() {
        super.onResume()

        if (mService != null) {
            if (mService!!.getState() === BluetoothService.STATE_NONE) {
                // Start the Bluetooth services
                mService!!.start()
            }
        }
    }

    @Synchronized public override fun onPause() {
        super.onPause()
        if (DEBUG)
            Log.e(TAG, "- ON PAUSE -")
    }

    public override fun onStop() {
        super.onStop()
        if (DEBUG)
            Log.e(TAG, "-- ON STOP --")
    }

    public override fun onDestroy() {
        super.onDestroy()
        // Stop the Bluetooth services
        if (mService != null)
            mService!!.stop()
        if (DEBUG)
            Log.e(TAG, "--- ON DESTROY ---")
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (DEBUG)
            Log.d(TAG, "onActivityResult " + resultCode)
        when (requestCode) {
            REQUEST_CONNECT_DEVICE -> {
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    // Get the device MAC address
                    val address = data!!.extras!!.getString(
                            DeviceListActivity.EXTRA_DEVICE_ADDRESS)
                    // Get the BLuetoothDevice object
                    if (BluetoothAdapter.checkBluetoothAddress(address)) {
                        val device = mBluetoothAdapter!!.getRemoteDevice(address)
                        // Attempt to connect to the device
                        mService!!.connect(device)
                    }
                }
            }
            REQUEST_ENABLE_BT -> {
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                    // Bluetooth is now enabled, so set up a session
                    keyListenerInit()
                } else {
                    // User did not enable Bluetooth or an error occured
                    Log.d(TAG, "BT not enabled")
                    Toast.makeText(this, R.string.bt_not_enabled_leaving,
                            Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
            REQUEST_CHOSE_BMP -> {
            }
            REQUEST_CAMER -> {
            }
        }
    }

    private fun keyListenerInit() {
        mService = BluetoothService(this, mHandler)
    }

    fun write(string: String) {
        mService!!.write(getBytes(string))
    }

    fun writeWithFormat(string: String, format: String) {

        when (format) {
            FORMAT_LEFT -> mService!!.writeWithFormat(getBytes(string), Formatter().get(), Formatter.leftAlign())
            FORMAT_CENTER -> mService!!.writeWithFormat(getBytes(string), Formatter().get(), Formatter.centerAlign())
            FORMAT_RIGHT -> mService!!.writeWithFormat(getBytes(string), Formatter().get(), Formatter.rightAlign())
        }
    }

    fun writeWithFormat(string: String, format: String, style: String) {
        val formatStyle = when(style){
            STYLE_BOLD -> Formatter().bold().get()
            STYLE_SMALL -> Formatter().small().get()
            else -> { Formatter().get() }
        }

        when (format) {
            FORMAT_LEFT -> mService!!.writeWithFormat(getBytes(string), formatStyle, Formatter.leftAlign())
            FORMAT_CENTER -> mService!!.writeWithFormat(getBytes(string), formatStyle, Formatter.centerAlign())
            FORMAT_RIGHT -> mService!!.writeWithFormat(getBytes(string), formatStyle, Formatter.rightAlign())
        }
    }

    fun writeEnter() {
        write("\n")
    }

    fun writeSpasi(panjang: Int) {
        val spasi = StringBuilder()
        for (i in 0 until panjang) {
            spasi.append(" ")
        }

        write(spasi.toString())
    }

    fun getBytes(string: String): ByteArray? {
        try {
            return string.toByteArray(charset("GBK"))
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }

        return null
    }
}
