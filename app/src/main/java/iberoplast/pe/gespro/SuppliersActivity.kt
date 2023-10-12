package iberoplast.pe.gespro

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import iberoplast.pe.gespro.model.Supplier

class SuppliersActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_suppliers)

        val suppliers = ArrayList<Supplier>()
        suppliers.add(Supplier(1, "Juanito", "Juanito@gmail.com"))
        suppliers.add(Supplier(2, "Alfonso", "Alfonso@gmail.com"))
        suppliers.add(Supplier(3, "Daniel", "Daniel@gmail.com"))

        val rvSuppliers = findViewById<RecyclerView>(R.id.rvProveedores)
        rvSuppliers.layoutManager = LinearLayoutManager(this)
        rvSuppliers.adapter = SupplierAdapter(suppliers)

    }
}