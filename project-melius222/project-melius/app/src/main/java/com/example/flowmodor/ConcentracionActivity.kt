package com.example.flowmodor

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.jjoe64.graphview.DefaultLabelFormatter
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.helper.StaticLabelsFormatter
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


class ConcentracionActivity : AppCompatActivity() {
    lateinit var lineGraphView: GraphView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_concentracion)

        // Obtener referencia al TextView para mostrar la fecha actual
        val txtDate = findViewById<TextView>(R.id.textView_date)

        // Obtener la fecha actual y formatearla como "dd/MM/yyyy"
        val currentDateT = Date()
        val dateFormatT = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val formattedDateT = dateFormatT.format(currentDateT)
        // Mostrar la fecha formateada en el TextView
        txtDate.text = formattedDateT

        lineGraphView = findViewById(R.id.idGraphView)

        val metodoEstudio = findViewById<TextView>(R.id.MetodoSesionCon)
        val conAlta = findViewById<TextView>(R.id.NotaCon)

        val db = FirebaseFirestore.getInstance()
        val userEmail = FirebaseAuth.getInstance().currentUser!!.email
        val documentReference = db.collection("estudio").document(userEmail!!).collection("dias")
        val query = documentReference
        val tasks = mutableListOf<Task<QuerySnapshot>>()
        tasks.add(query.get())


        val fechaLimite = Calendar.getInstance().apply { add(Calendar.DATE, -7) }.time
        val dateList = mutableListOf<String>()

        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        // Obtener fechas de los últimos 7 días
        for (i in 0..6) {
            val fecha = Calendar.getInstance().apply { add(Calendar.DATE, -i) }.time
            val fechaFormateada = formatter.format(fecha)
            dateList.add(fechaFormateada)
        }



        var notaConcArray = mutableListOf<Int>()
        var notaProduArray = mutableListOf<Int>()
        var metodoArray = mutableListOf<Int>()
        var fechaArray = mutableListOf<String>()
        var arrayMetodoCopia = mutableListOf<Int>()
        var numRondasArray = mutableListOf<Int>()
        for (fecha in dateList) {

            var notaConc = 0
            var notaProd = 0
            var docFound = false
            Tasks.whenAllSuccess<QuerySnapshot>(tasks)
                .addOnSuccessListener { querySnapshots ->
                    for (querySnapshot in querySnapshots) {
                        for (document in querySnapshot.documents) {
                            val fechaDocStr = document.getString("formattedDate")
                            val fechaDoc =
                                SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(
                                    fechaDocStr
                                )
                            val fechaStr =
                                SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(
                                    fecha
                                )
                            if (fechaDoc == fechaStr) {
                                val metodo =
                                    document.getLong("metodo")?.toInt() ?: -1 // -1 si no hay valor
                                val numRondas = document.getLong("numRondas")?.toInt()
                                    ?: -1 // -1 si no hay valor
                                notaProd =
                                    document.getLong("notaProdu")?.toInt() ?: 0 // 0 si no hay valor
                                notaConc =
                                    document.getLong("notaConc")?.toInt() ?: 0 // 0 si no hay valor
                                val notaEstado = document.getLong("notaEstado")?.toInt()
                                    ?: 0 // 0 si no hay valor
                                val fechaDocStr = document.getString("formattedDate")
                                notaConcArray.add(notaConc)
                                notaProduArray.add(notaProd)
                                metodoArray.add(metodo)
                                fechaArray.add(fechaDocStr!!)
                                numRondasArray.add(numRondas)
                                docFound = true
                                break
                            }
                        }
                        if (docFound) break
                    }
                    if (!docFound) { 
                        notaConcArray.add(0)
                        notaProduArray.add(0)
                        metodoArray.add(-1)
                    }
                    val tam = notaConcArray.size
                    if(notaConcArray.size > 6){
                        val series: LineGraphSeries<DataPoint> = LineGraphSeries(
                            arrayOf(
                                // on below line we are adding
                                // each point on our x and y axis.
                                DataPoint(0.0, notaConcArray[6].toDouble()),
                                DataPoint(1.0, notaConcArray[5].toDouble()),
                                DataPoint(2.0, notaConcArray[4].toDouble()),
                                DataPoint(3.0, notaConcArray[3].toDouble()),
                                DataPoint(4.0, notaConcArray[2].toDouble()),
                                DataPoint(5.0, notaConcArray[1].toDouble()),
                                DataPoint(6.0, notaConcArray[0].toDouble()),
                            )
                        )

                        // Configurar los límites del eje X e Y
                        lineGraphView.viewport.isXAxisBoundsManual = true
                        lineGraphView.viewport.setMinX(0.0)
                        lineGraphView.viewport.setMaxX(6.0)
                        lineGraphView.viewport.isYAxisBoundsManual = true
                        lineGraphView.viewport.setMinY(0.0)
                        lineGraphView.viewport.setMaxY(5.0)
// set color for series
                        series.setColor(ContextCompat.getColor(this, R.color.verdeFlow))
                        series.setThickness(10)
                        series.setDrawDataPoints(true)
                        series.setDataPointsRadius(8f)
                        series.setDrawBackground(true)
                        series.setBackgroundColor(ContextCompat.getColor(this, R.color.rojoFlowTrans))
                        series.setAnimated(true)
                        val staticLabelsFormatter = StaticLabelsFormatter(lineGraphView)
                        staticLabelsFormatter.setHorizontalLabels(arrayOf("","", "", "", ""))
                        staticLabelsFormatter.setVerticalLabels(arrayOf("0", "1", "2", "3", "4", "5"))
                        lineGraphView.gridLabelRenderer.labelFormatter = staticLabelsFormatter

                        // Establecer título del gráfico
                        lineGraphView.setTitle("GRÁFICO DE CONCENTRACIÓN")

                        // Establecer título del eje horizontal
                        lineGraphView.getGridLabelRenderer().setHorizontalAxisTitle("Últimos 7 días")

                        // Establecer título del eje vertical
                        lineGraphView.getGridLabelRenderer().setVerticalAxisTitle("Rango de Concentración")

                        // add animation
                        lineGraphView.animate()

                        // set scrollable for point graph view
                        lineGraphView.viewport.isScrollable = false

                        // set scalable
                        lineGraphView.viewport.isScalable = false

                        // set scalable y
                        lineGraphView.viewport.setScalableY(false)

                        // set scrollable y
                        lineGraphView.viewport.setScrollableY(false)


                        // add data series to our graph view
                        lineGraphView.addSeries(series)

                        for (p in metodoArray){
                            if(p > -1)
                            {
                                arrayMetodoCopia.add(p)
                            }
                        }

                        //SESION MÁS USADA
                        val elementoMasComun = arrayMetodoCopia.groupingBy { it }
                            .eachCount()
                            .entries
                            .maxByOrNull { it.value }
                            ?.key
                        if(elementoMasComun == 0){
                            metodoEstudio.text = "Flowmodoro"
                        }else if(elementoMasComun == 1){
                            metodoEstudio.text = "Pomodoro"
                        }else if(elementoMasComun == 2){
                            metodoEstudio.text = "High Tasking"
                        }else{
                            metodoEstudio.text = "No encontrado"
                        }

                        //MEJOR PRODUCTIVIDAD
                        // Crear un mapa para almacenar la suma de notas y el número de veces que se utiliza cada método
                        val metodoMap = mutableMapOf<Int, Pair<Int, Int>>()

                        if (notaConcArray.size == metodoArray.size) {
                            for (i in notaConcArray.indices) {
                                val metodo = metodoArray[i]
                                val notaProdu = notaConcArray[i]
                                // Resto del código para actualizar el mapa y calcular la media
                                if (metodoMap.containsKey(metodo)) {
                                    val pair = metodoMap[metodo]!!
                                    val sum = pair.first + notaProdu
                                    val count = pair.second + 1
                                    metodoMap[metodo] = Pair(sum, count)
                                } else {
                                    metodoMap[metodo] = Pair(notaProdu, 1)
                                }

                                // Calcular la media de cada método y encontrar el método con la media más alta
                                var maxMedia = 0.0
                                var maxMetodo = 0
                                for ((metodo, pair) in metodoMap) {
                                    val sum = pair.first
                                    val count = pair.second
                                    val media = sum.toDouble() / count
                                    if (media > maxMedia) {
                                        maxMedia = media
                                        maxMetodo = metodo
                                    }
                                }
                                // muestra el resultado
                                if(maxMetodo == 0){
                                    conAlta.text = "Flowmodoro"
                                }else if(maxMetodo == 1){
                                    conAlta.text = "Pomodoro"
                                }else if(maxMetodo == 2){
                                    conAlta.text = "High Tasking"
                                }else{
                                    conAlta.text = "No encontrado"
                                }
                            }
                        } else {
                            // Las listas tienen diferentes tamaños, manejar el error aquí
                            conAlta.text = "No ha realizado una semana de estudios"
                        }


                    }
                }

        }

        //metodoArray.add(metodo.toInt())
        //fechaArray.add(fechaDocStr.toString())
        //numRondasArray.add(numRondas.toInt())


        // on below line we are adding data to our graph view.

        // Boton Home
        val botonHome = findViewById<ImageView>(R.id.btn_home)

        botonHome.setOnClickListener {
            // Código para abrir la pantalla de Flowmodoro
            val intentMain = Intent(this, MainActivity::class.java)
            startActivity(intentMain)
        }

    }


}