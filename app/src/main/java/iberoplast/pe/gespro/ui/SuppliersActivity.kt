package iberoplast.pe.gespro.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import iberoplast.pe.gespro.R
import iberoplast.pe.gespro.model.Supplier

class SuppliersActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_suppliers)

        val suppliers = ArrayList<Supplier>()
        suppliers.add(Supplier(1, "Juanito", "Juanito@gmail.com", "Rechazado"))
        suppliers.add(Supplier(2, "Alfonso", "Alfonso@gmail.com", "Aprobado"))
        suppliers.add(Supplier(3, "Daniel", "Daniel@gmail.com", "Por validar"))

        val rvSuppliers = findViewById<RecyclerView>(R.id.rvProveedores)
        rvSuppliers.layoutManager = LinearLayoutManager(this)
        rvSuppliers.adapter = SupplierAdapter(suppliers)

        // Registra el RecyclerView para el menú contextual
//        registerForContextMenu(rvSuppliers)

    }

//    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
//        super.onCreateContextMenu(menu, v, menuInfo)
//
//        if (v?.id == R.id.rvProveedores) {
//            val inflater: MenuInflater = menuInflater
//            inflater.inflate(R.menu.menu_options, menu)
//        }
//    }

//    override fun onContextItemSelected(item: MenuItem): Boolean {
//        // Maneja las opciones del menú contextual aquí
//        when (item.itemId) {
//            R.id.opc2 -> {
//                // Lógica para la opción 1
//                return true
//            }
//            R.id.opc1 -> {
//                // Lógica para la opción 2
//                return true
//            }
//            else -> return super.onContextItemSelected(item)
//        }
//    }
}