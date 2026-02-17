package com.example.legalconverter // अपने पैकेज का नाम यहाँ लिखें

import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.google.android.material.textfield.TextInputEditText

class MainActivity : AppCompatActivity() {

    // डेटा मॉडल (BNS -> IPC मैपिंग)
    data class LegalSection(val ipcSection: String, val title: String, val note: String)

    // हार्डकोडेड डेटाबेस (इसे बाद में 1000+ सेक्शन्स के लिए JSON या SQLite से बदला जा सकता है)
    private val sectionDatabase = hashMapOf(
        "103" to LegalSection("IPC 302", "Murder (हत्या)", "BNS Section 103 now covers Murder, replacing IPC 302."),
        "103(1)" to LegalSection("IPC 302", "Murder", "Sub-section for specific categories."),
        "105" to LegalSection("IPC 304", "Culpable Homicide (गैर इरादतन हत्या)", "Not amounting to murder."),
        "64" to LegalSection("IPC 376", "Rape (दुष्कर्म)", "Punishment for Rape."),
        "69" to LegalSection("New Provision", "Sexual Intercourse by Deceit", "Identity concealment/False promise to marry (Love Jihad context)."),
        "318" to LegalSection("IPC 420", "Cheating (धोखाधड़ी)", "Cheating and dishonestly inducing delivery of property."),
        "303(2)" to LegalSection("IPC 379", "Theft (चोरी)", "Punishment for theft."),
        "111" to LegalSection("New Provision", "Organized Crime", "No direct IPC equivalent, derived from MCOCA/UAPA concepts."),
        "152" to LegalSection("IPC 124A", "Sedition (राजद्रोह - बदल गया)", "Replaced 'Sedition' with 'Acts endangering sovereignty'.")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val etInput = findViewById<TextInputEditText>(R.id.etBnsInput)
        val btnSearch = findViewById<Button>(R.id.btnSearch)
        val resultCard = findViewById<CardView>(R.id.resultCard)
        val tvIpcResult = findViewById<TextView>(R.id.tvIpcResult)
        val tvDescription = findViewById<TextView>(R.id.tvDescription)
        val tvDetails = findViewById<TextView>(R.id.tvDetails)

        btnSearch.setOnClickListener {
            // कीबोर्ड छिपाएं
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)

            val query = etInput.text.toString().trim()

            if (query.isEmpty()) {
                etInput.error = "Please enter a section"
                return@setOnClickListener
            }

            // डेटाबेस में सर्च करें
            val result = sectionDatabase[query]

            if (result != null) {
                // मिल गया! कार्ड दिखाएं
                resultCard.visibility = View.VISIBLE
                tvIpcResult.text = result.ipcSection
                tvDescription.text = result.title
                tvDetails.text = result.note
            } else {
                // नहीं मिला
                resultCard.visibility = View.GONE
                Toast.makeText(this, "Section $query not found in database yet.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
