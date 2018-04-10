package com.example.printersds.testprintclass

import com.example.printersds.MainActivity
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by hafizdwp on 4/10/2018.
 */
class Dropping(private val activity: MainActivity) {

    /***********************************************************************************************/

    /*header variable*/
    val DISTRIBUTOR = "PT. Wangsa Retail Nusantara"
    val ALAMAT = "Jl. Rengasdengklok no 26,Bandung"
    val NOTA = "NOTA DROPPING"

    /*headerdua variable*/
    val NOMOR_NOMORAN = "DN/716170475/180209"
    val TANGGAL = SimpleDateFormat("dd/MM/yyyy").format(Date())
    val NOMOR_NOMORAN_TOKO = "[716170475] MULYA STORE - MM (716170475)"
    val DISKON_TOKO = "Disc 15%"
    val NAMA_SALESMAN = "NTIS SUTISNA"

    /*item variable*/
    val arrayItem = arrayOf("T. SPECIAL", "T. GANDUM", "J. TAWAR SPSIAL", "JUMBO KUPAS", "T. DOUBLE SOFT", "T. MINI SOFT", "T. KUPAS", "S. COKLAT", "SAND. KACANG", "SJCK")
    val arrayJumlah = arrayOf("7", "3", "1", "1", "5", "3", "7", "5", "5", "5")
    val arrayHarga = arrayOf("84,000", "54,000", "15,000", "18,000", "90,000", "36,000", "101,500", "20,000", "20,000", "20,000")

    /*footer variable*/
    val nomSubtotal = "458,500"
    val nomDiskon = "68,775"
    val nomTotal = "389,725"

    /***********************************************************************************************/

    fun print() {
        printHeader(DISTRIBUTOR, ALAMAT, NOTA)
        printHeaderDua(NOMOR_NOMORAN, TANGGAL, NOMOR_NOMORAN_TOKO, DISKON_TOKO, NAMA_SALESMAN)
        printItem(arrayItem, arrayJumlah, arrayHarga)
        printFooter(nomSubtotal, nomDiskon, nomTotal)
    }

    /**
     * HEADER
     */
    private fun printHeader(distributor: String, alamat: String, nota: String) {
        with(activity) {
            writeWithFormat(distributor, FORMAT_CENTER)
            writeEnter()

            writeWithFormat(alamat, FORMAT_CENTER)
            writeEnter()

            writeWithFormat(nota, FORMAT_CENTER)
            writeEnter()
            writeEnter()
        }
    }

    /**
     * HEADER 2
     */
    private fun printHeaderDua(nonomoran: String,
                                tanggal: String,
                                nonomoranToko: String,
                                diskon: String,
                                namaSalesman: String) {
        with(activity) {
            writeWithFormat(nonomoran, FORMAT_LEFT)
            writeSpasi(MAX_CHAR - nonomoran.length - tanggal.length)
            write(tanggal)
            writeEnter()

            writeWithFormat(nonomoranToko, FORMAT_LEFT)
            writeEnter()

            writeWithFormat(diskon, FORMAT_LEFT)
            writeSpasi(MAX_CHAR - diskon.length - namaSalesman.length)
            write(namaSalesman)
            writeEnter()
        }
    }

    /**
     * ITEM
     */
    private fun printItem(item: Array<String>, jumlah: Array<String>, harga: Array<String>) {
        with(activity){
            write("--------------------------------")
            writeEnter()

            for (i in item.indices) {
                printItemAll(arrayItem[i], arrayJumlah[i], arrayHarga[i])
                writeEnter()
            }

            write("--------------------------------")
            writeEnter()
        }
    }

    private fun printItemAll(kiri: String, tengah: String, kanan: String) {
        printItemKiri(kiri)
        printItemTengah(tengah)
        printItemKanan(kanan)
    }

    private fun printItemKiri(text: String) {
        with(activity){
            writeWithFormat(text, FORMAT_LEFT)
            writeSpasi(MAX_KIRI - text.length)
        }
    }

    private fun printItemTengah(text: String) {
        with(activity){
            write(text)
            writeSpasi(MAX_TENGAH - text.length)
        }
    }

    private fun printItemKanan(text: String) {
        with(activity){
            writeSpasi(MAX_KANAN - text.length)
            write(text)
        }
    }

    /**
     * FOOTER
     */
    private fun printFooter(nominalSubtotal: String, nominalDiskon: String, nominalTotal: String) {
        with(activity){
            writeWithFormat("Subtotal", FORMAT_LEFT)
            writeSpasi(MAX_CHAR - "Subtotal".length - nominalSubtotal.length)
            write(nominalSubtotal)
            writeEnter()

            writeWithFormat(DISKON_TOKO, FORMAT_LEFT)
            writeSpasi(MAX_CHAR - DISKON_TOKO.length - nominalDiskon.length)
            write(nominalDiskon)
            writeEnter()

            writeWithFormat("Total Drop", FORMAT_LEFT)
            writeSpasi(MAX_CHAR - "Total Drop".length - nominalTotal.length)
            write(nominalTotal)
            writeEnter()
        }
    }
}