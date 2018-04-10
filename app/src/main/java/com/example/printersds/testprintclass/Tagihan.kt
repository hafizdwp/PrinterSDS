package com.example.printersds.testprintclass

import com.example.printersds.MainActivity

/**
 * Created by hafizdwp on 4/10/2018.
 */
class Tagihan(private val activity: MainActivity) {

    /*header variable*/
    val DISTRIBUTOR = "PT. Wangsa Retail Nusantara"
    val ALAMAT = "Jl. Rengasdengklok no 26,Bandung"
    val NOTA = "NOTA TAGIHAN"

    /*headerdua variable*/
    val NAMA_TOKO = "AS-SALAM - MINIMARKET"
    val NONOMORAN = "TokoDEF/03/2018"
    val NAMA_SALESMAN = "Coba Test100"

    /*item variable*/
    val arrayItem = arrayOf("RTS", "RTS6", "RCC", "ICK")
    val arrayJumlah = arrayOf("10", "10", "3", "3")
    val arrayHarga = arrayOf("115,000", "65,000", "40,500", "16,500")

    val arrayReturBaik = arrayOf("3", "3", "0", "0")
    val arrayReturBS = arrayOf("2", "0", "1", "0")
    val arrayHargaRB = arrayOf("34,500", "19,500", "0", "0")
    val arrayHargaRBS = arrayOf("23,000", "0", "13,500", "0")

    /*footer variable*/
    val nomJumlah = "183,000"
    val nomDiskon = "18,300"
    val nomTotal = "165,000"
    val nomTerbayar = "150,000"
    val nomSisa = "15,000"

    fun print() {
        printHeader(DISTRIBUTOR, ALAMAT, NOTA)
        printHeaderDua(NAMA_TOKO, NONOMORAN, NAMA_SALESMAN)
        printItem(arrayItem, arrayJumlah, arrayHarga, arrayReturBaik, arrayReturBS, arrayHargaRB, arrayHargaRBS)
        printFooter(nomJumlah, nomDiskon, nomTotal, nomTerbayar, nomSisa)
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
     * HEADER DUA
     * */
    private fun printHeaderDua(namaToko: String, nonomoran: String, namaSalesman: String) {
        with(activity) {
            writeWithFormat(namaToko, FORMAT_LEFT)
            writeEnter()

            writeWithFormat(nonomoran, FORMAT_LEFT)
            writeEnter()

            val salesmanLabel = "Salesman :"
            writeWithFormat(salesmanLabel, FORMAT_LEFT)
            writeSpasi(MAX_CHAR - salesmanLabel.length - namaSalesman.length)
            write(namaSalesman)
            writeEnter()
        }
    }

    /**
     * ITEM
     **/
    private fun printItem(arrayItem: Array<String>,
                          arrayJumlah: Array<String>,
                          arrayHarga: Array<String>,
                          arrayReturBaik: Array<String>,
                          arrayReturBS: Array<String>,
                          arrayHargaRB: Array<String>,
                          arrayHargaRBS: Array<String>) {
        with(activity) {
            write("--------------------------------")
            writeEnter()

            for (i in arrayItem.indices) {
                printItemAll(arrayItem[i],
                        arrayJumlah[i],
                        arrayHarga[i],
                        arrayReturBaik[i],
                        arrayReturBS[i],
                        arrayHargaRB[i],
                        arrayHargaRBS[i])
                writeEnter()

                if (i + 1 != arrayItem.size) writeEnter()
            }

            write("--------------------------------")
            writeEnter()
        }
    }

    private fun printItemAll(item: String,
                             jumlah: String,
                             harga: String,
                             returBaik: String,
                             returBS: String,
                             hargaRB: String,
                             hargaRBS: String) {

        val LABEL_RB = "Retur Baik"
        val LABEL_RBS = "Retur BS"

        with(activity) {
            printItemKiri(item)
            printItemTengah(jumlah)
            printItemKanan(harga)

            if (returBaik != "0") {
                writeEnter()
                printItemKiri("  $LABEL_RB")
                printItemTengah(returBaik)
                printItemKanan(hargaRB)
            }

            if (returBS != "0") {
                writeEnter()
                printItemKiri("  $LABEL_RBS")
                printItemTengah(returBS)
                printItemKanan(hargaRBS)
            }
        }
    }

    private fun printItemKiri(text: String) {
        with(activity) {
            writeWithFormat(text, FORMAT_LEFT)
            writeSpasi(MAX_KIRI - text.length)
        }
    }

    private fun printItemTengah(text: String) {
        with(activity) {
            write(text)
            writeSpasi(MAX_TENGAH - text.length)
        }
    }

    private fun printItemKanan(text: String) {
        with(activity) {
            writeSpasi(MAX_KANAN - text.length)
            write(text)
        }
    }

    /**
     * FOOTER
     * */
    private fun printFooter(nomJumlah: String, nomDiskon: String, nomTotal: String, nomTerbayar: String, nomSisa: String) {

        val LABEL_JUMLAH = "Jumlah"
        val LABEL_DISKON = "Diskon"
        val LABEL_TOTAL = "Total"
        val LABEL_TERBAYAR = "Terbayar"
        val LABEL_SISA = "Sisa"

        with(activity) {
            writeWithFormat(LABEL_JUMLAH, FORMAT_LEFT)
            writeSpasi(MAX_CHAR - LABEL_JUMLAH.length - nomJumlah.length)
            write(nomJumlah)
            writeEnter()

            writeWithFormat(LABEL_DISKON, FORMAT_LEFT)
            writeSpasi(MAX_CHAR - LABEL_DISKON.length - nomDiskon.length)
            write(nomDiskon)
            writeEnter()

            writeWithFormat(LABEL_TOTAL, FORMAT_LEFT)
            writeSpasi(MAX_CHAR - LABEL_TOTAL.length - nomTotal.length)
            write(nomTotal)
            writeEnter()

            writeWithFormat(LABEL_TERBAYAR, FORMAT_LEFT)
            writeSpasi(MAX_CHAR - LABEL_TERBAYAR.length - nomTerbayar.length)
            write(nomTerbayar)
            writeEnter()

            writeWithFormat(LABEL_SISA, FORMAT_LEFT)
            writeSpasi(MAX_CHAR - LABEL_SISA.length - nomSisa.length)
            write(nomSisa)
            writeEnter()

            writeEnter()
        }
    }
}