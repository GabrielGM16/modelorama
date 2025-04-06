package com.example.modelorama

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.modelorama.databinding.ActivityTicketBinding
import com.example.modelorama.utils.QRCodeGenerator
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TicketActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTicketBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTicketBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupUI()
        loadTicketData()
    }
    
    private fun setupUI() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Ticket de compra"
        
        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        
        binding.buttonHome.setOnClickListener {
            // Volver a la pantalla principal
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
        
        binding.buttonShare.setOnClickListener {
            // Compartir información del pedido
            val orderId = intent.getStringExtra("ORDER_ID") ?: ""
            val totalPrice = intent.getDoubleExtra("TOTAL_PRICE", 0.0)
            
            val shareText = "¡He realizado un pedido en Modelorama!\n" +
                    "Número de pedido: $orderId\n" +
                    "Total: $${totalPrice}"
            
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, shareText)
                type = "text/plain"
            }
            startActivity(Intent.createChooser(shareIntent, "Compartir pedido"))
        }
    }
    
    private fun loadTicketData() {
        val orderId = intent.getStringExtra("ORDER_ID") ?: ""
        val customerName = intent.getStringExtra("CUSTOMER_NAME") ?: ""
        val customerAddress = intent.getStringExtra("CUSTOMER_ADDRESS") ?: ""
        val customerPhone = intent.getStringExtra("CUSTOMER_PHONE") ?: ""
        val totalItems = intent.getIntExtra("TOTAL_ITEMS", 0)
        val totalPrice = intent.getDoubleExtra("TOTAL_PRICE", 0.0)
        
        // Formatear la fecha actual
        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val currentDate = dateFormat.format(Date())
        
        // Mostrar los datos del ticket
        binding.textViewOrderId.text = "Pedido #$orderId"
        binding.textViewDate.text = "Fecha: $currentDate"
        binding.textViewCustomerName.text = "Cliente: $customerName"
        binding.textViewCustomerAddress.text = "Dirección: $customerAddress"
        binding.textViewCustomerPhone.text = "Teléfono: $customerPhone"
        binding.textViewTotalItems.text = "Total de productos: $totalItems"
        binding.textViewTotalPrice.text = "Total a pagar: $${totalPrice}"
        
        // Generar y mostrar el código QR
        val qrContent = "PEDIDO:$orderId\nCLIENTE:$customerName\nTOTAL:$${totalPrice}"
        val qrCodeBitmap = QRCodeGenerator.generateQRCode(qrContent, 300, 300)
        binding.imageViewQrCode.setImageBitmap(qrCodeBitmap)
        
        // Mostrar el estado del pedido
        binding.textViewOrderStatus.text = "Estado: Pendiente"
    }
}