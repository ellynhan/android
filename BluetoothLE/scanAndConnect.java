package androidble.ble_java;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.ParcelUuid;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    //// GUI variables
    // text view for status
    private TextView tv_status_;
    // text view for read
    private TextView tv_read_;
    // button for start scan
    private Button btn_scan_;
    // button for stop connection
    private Button btn_stop_;
    // button for send data
    private Button btn_send_;
    // button for show paired devices
    private Button btn_show_;

    // Tag name for Log message
    private final static String TAG="Central";
    // used to identify adding bluetooth names
    private final static int REQUEST_ENABLE_BT= 1;
    // used to request fine location permission
    private final static int REQUEST_FINE_LOCATION= 2;
    // scan period in milliseconds
    private final static int SCAN_PERIOD= 5000;
    // ble adapter
    private BluetoothAdapter ble_adapter_;
    // flag for scanning
    private boolean is_scanning_= false;
    // flag for connection
    private boolean connected_= false;
    // scan results
    private Map<String, BluetoothDevice> scan_results_;
    // scan callback
    private ScanCallback scan_cb_;
    // ble scanner
    private BluetoothLeScanner ble_scanner_;
    // scan handler
    private Handler scan_handler_;

    private BluetoothGatt ble_gatt_;

    public static String SERVICE_STRING = "0000aab0-f845-40fa-995d-658a43feea4c";
    public static UUID UUID_TDCS_SERVICE= UUID.fromString(SERVICE_STRING);
    public static String CHARACTERISTIC_COMMAND_STRING = "0000AAB1-F845-40FA-995D-658A43FEEA4C";
    public static UUID UUID_CTRL_COMMAND = UUID.fromString( CHARACTERISTIC_COMMAND_STRING );
    public static String CHARACTERISTIC_RESPONSE_STRING = "0000AAB2-F845-40FA-995D-658A43FEEA4C";
    public static UUID UUID_CTRL_RESPONSE = UUID.fromString( CHARACTERISTIC_RESPONSE_STRING );
    public final static String MAC_ADDR= "78:A5:04:58:A7:92";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //// get instances of gui objects
        // status textview
        tv_status_= findViewById( R.id.tv_status );
        // read textview
        tv_read_= findViewById( R.id.tv_read );
        // scan button
        btn_scan_= findViewById( R.id.btn_scan );
        // stop button
        btn_stop_= findViewById( R.id.btn_stop );
        // send button
        btn_send_= findViewById( R.id.btn_send );
        // show button
        btn_show_= findViewById( R.id.btn_show );

        // ble manager
        BluetoothManager ble_manager;
        ble_manager= (BluetoothManager)getSystemService(Context.BLUETOOTH_SERVICE );
        // set ble adapter
        ble_adapter_= ble_manager.getAdapter();
        //// set click event handler
        btn_scan_.setOnClickListener( (v) -> { startScan(v); });
    }

    @Override
    protected void onResume() {
        super.onResume();

        // finish app if the BLE is not supported
        if( !getPackageManager().hasSystemFeature( PackageManager.FEATURE_BLUETOOTH_LE ) ) {
            finish();
        }
    }

    /*
    Request BLE enable
    */
    private void requestEnableBLE() {
        Intent ble_enable_intent= new Intent( BluetoothAdapter.ACTION_REQUEST_ENABLE );
        startActivityForResult( ble_enable_intent, REQUEST_ENABLE_BT );

    }

    /*
    Request Fine Location permission
     */
    private void requestLocationPermission() {
        requestPermissions( new String[]{ Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_FINE_LOCATION );
    }

    /*
    Start BLE scan
     */
    private void startScan( View v ) {
        tv_status_.setText("Scanning...");
        // check ble adapter and ble enabled
        if (ble_adapter_ == null || !ble_adapter_.isEnabled()) {
            requestEnableBLE();
            tv_status_.setText("Scanning Failed: ble not enabled");
            return;
        }
        // check if location permission
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestLocationPermission();
            tv_status_.setText("Scanning Failed: no fine location permission");
            return;
        }
        // disconnect gatt server
        disconnectGattServer(); //기존에 연결된 서버와 연결을 종료하도록 호출

        // setup scan filters
        List<ScanFilter> filters= new ArrayList<>();
        ScanFilter scan_filter= new ScanFilter.Builder()
                .setServiceUuid( new ParcelUuid( UUID_TDCS_SERVICE ) )
                .build();
//        filters.add( scan_filter );
        //// scan settings
        // set low power scan mode
        ScanSettings settings= new ScanSettings.Builder()
                .setScanMode( ScanSettings.SCAN_MODE_LOW_POWER )
                .build();
        scan_results_= new HashMap<>();
        scan_cb_= new BLEScanCallback( scan_results_ );
        //// now ready to scan
        // start scan
        ble_scanner_ = ble_adapter_.getBluetoothLeScanner();
        System.out.print("getClass done");
        ble_scanner_.startScan( filters, settings, scan_cb_ );
        // set scanning flag
        is_scanning_= true;
        scan_handler_ = new Handler();
        scan_handler_.postDelayed(this::stopScan, SCAN_PERIOD);

    }

    /*
    Stop scanning
     */
    private void stopScan(){
        if(is_scanning_ && ble_adapter_ != null && ble_adapter_.isEnabled() && ble_scanner_ !=null){
            ble_scanner_.stopScan(scan_cb_);
        }
        scan_cb_ = null;
        is_scanning_=false;
        scan_handler_=null;
        tv_status_.setText("Scanning stopped");
        scanComplete();
    }

    /*
    Handle scan results after scan stopped
     */
    private void scanComplete() {
        if( scan_results_.isEmpty() ) {
            tv_status_.setText( "scan results is empty" );
            Log.d( TAG, "scan results is empty" );
            return;
        }

        for( String device_addr : scan_results_.keySet() ) {
            Log.d( TAG, "Found device: " + device_addr );
//            BluetoothDevice device= scan_results_.get( device_addr );
//            if( MAC_ADDR.equals( device_addr) ) {
//                Log.d( TAG, "connecting device: " + device_addr );
                // connect to the device
//                connectDevice(device);
//            }
        }
    }

    public void selectAndConnectDevice(String macAddress){
        BluetoothDevice selectedDeviceInstance = scan_results_.get(macAddress);
        connectDevice(selectedDeviceInstance);
    }


    private void connectDevice( BluetoothDevice _device ) {
        // update the status
        tv_status_.setText( "Connecting to " + _device.getAddress() );
        GattClientCallback  gatt_client_cb = new GattClientCallback ();
        ble_gatt_= _device.connectGatt( this, false, gatt_client_cb ); //autoConnect false로 자동연결 안되게함
    }

    private class GattClientCallback extends BluetoothGattCallback {
        @Override
        public void onConnectionStateChange( BluetoothGatt _gatt, int _status, int _new_state ) {
            super.onConnectionStateChange( _gatt, _status, _new_state );
            if( _status == BluetoothGatt.GATT_FAILURE ) {
                disconnectGattServer();
                return;
            } else if( _status != BluetoothGatt.GATT_SUCCESS ) {
                disconnectGattServer();
                return;
            }
            if( _new_state == BluetoothProfile.STATE_CONNECTED ) {
                // update the connection status message
                tv_status_.setText( "Connected" );
                // set the connection flag
                connected_= true;
                Log.d( TAG, "Connected to the GATT server" );
            } else if ( _new_state == BluetoothProfile.STATE_DISCONNECTED ) {
                disconnectGattServer();
            }
        }
    }

    /*
    Disconnect Gatt Server, 연결 실패시
    */
    public void disconnectGattServer() {
        Log.d( TAG, "Closing Gatt connection" );
        // reset the connection flag
        connected_= false;
        // disconnect and close the gatt
        if( ble_gatt_ != null ) {
            ble_gatt_.disconnect();
            ble_gatt_.close();
        }
    }


    /*
    BLE Scan Callback class
    */
    private class BLEScanCallback extends ScanCallback {
        private Map<String, BluetoothDevice> cb_scan_results_;

        /*
        Constructor
         */
        BLEScanCallback( Map<String, BluetoothDevice> _scan_results ) {
            cb_scan_results_= _scan_results;
        }

        @Override
        public void onScanResult( int _callback_type, ScanResult _result ) {
            Log.d( TAG, "onScanResult" );
            addScanResult( _result );
        }

        @Override
        public void onBatchScanResults( List<ScanResult> _results ) {
            for( ScanResult result: _results ) {
                addScanResult( result );
            }
        }

        @Override
        public void onScanFailed( int _error ) {
            Log.e( TAG, "BLE scan failed with code " +_error );
        }

        /*
        Add scan result
         */
        private void addScanResult( ScanResult _result ) {
            // get scanned device
            BluetoothDevice device= _result.getDevice();
            // get scanned device MAC address
            String device_address= device.getAddress();
            // add the device to the result list
            cb_scan_results_.put( device_address, device );
            // log
            Log.d( TAG, "scan results device1: " + device.getName() );
            Log.d( TAG, "scan results device2: " + device.getAddress() );

            tv_status_.setText( "add scanned device: " + device_address );
            Log.d("SCANED DEVICE:", device_address);
        }


    }
}
