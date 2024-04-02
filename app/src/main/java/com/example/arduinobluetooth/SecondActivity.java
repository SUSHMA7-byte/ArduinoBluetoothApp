package com.example.arduinobluetooth;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.Set;

public class SecondActivity extends AppCompatActivity {

    private static final int REQUEST_ENABLE_BT = 1;
    private BluetoothAdapter bluetoothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        Button bluetoothButton = findViewById(R.id.bluetoothButton1); // Find the button by ID
        bluetoothButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                turnOnBluetooth();
            }
        });
    }

    private void turnOnBluetooth() {
        if (bluetoothAdapter == null) {
            // Device doesn't support Bluetooth
            Toast.makeText(this, "Device doesn't support Bluetooth", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!bluetoothAdapter.isEnabled()) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                // Request Bluetooth permission if not granted
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.BLUETOOTH_CONNECT}, REQUEST_ENABLE_BT);
                return;
            }
            // Enable Bluetooth if permission granted
            bluetoothAdapter.enable();
            Toast.makeText(this, "Turning on Bluetooth...", Toast.LENGTH_SHORT).show();
            // You might want to add a delay here to wait for Bluetooth to turn on
        } else {
            // Bluetooth is already enabled
            Toast.makeText(this, "Bluetooth is already enabled", Toast.LENGTH_SHORT).show();
            // Display connected devices
            displayConnectedDevices();
        }
    }

    private void displayConnectedDevices() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();

        if (pairedDevices.size() > 0) {
            StringBuilder devicesList = new StringBuilder();
            for (BluetoothDevice device : pairedDevices) {
                devicesList.append(device.getName()).append("\n").append(device.getAddress()).append("\n\n");
            }
            // Display the list of connected devices
            Toast.makeText(this, "Connected Devices:\n" + devicesList.toString(), Toast.LENGTH_LONG).show();
        } else {
            // No paired devices found
            Toast.makeText(this, "No paired devices found", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == RESULT_OK) {
                // Bluetooth is enabled by the user
                Toast.makeText(this, "Bluetooth is enabled", Toast.LENGTH_SHORT).show();
                // Display connected devices
                displayConnectedDevices();
            } else {
                // User canceled or didn't enable Bluetooth
                Toast.makeText(this, "Bluetooth enabling canceled", Toast.LENGTH_SHORT).show();
            }
        }
    }
}