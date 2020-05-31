package com.example

import org.apache.spark.ml.linalg.BLAS.dot
import org.apache.spark.ml.linalg.{Vector, Vectors}
import org.apache.spark.rdd.RDD

class FTRL extends Serializable {

  val L1 = 1
  val L2 = 1
  val alpha = 0.1
  val belta = 1

  var N: Array[Double] = Array(1000)
  var W: Array[Double] = Array(1000)
  var Z: Array[Double] = Array(1000)

  def fit(data: RDD[(Int, Vector)], label: Double) : Unit = {
    var p = 0.0
    data.map{
      case (i, v) =>
        val sgn = if (Z(i) < 0 ) -1.0 else 1.0
        if (sgn * Z(i) <= L1) {
          W(i) = 0.0
        } else {
          W(i) = (sgn * L1 - Z(i)) /
            ((belta + math.sqrt(N(i))) / alpha + L2)
        }

        p = dot(v, Vectors.dense(W(i)))
    }

    val mult = label * (1 / (1 + Math.exp(-p * label)) - 1)

    data.map {
      case (i,v) =>
        val g = dot(Vectors.dense(mult), v)
        val sigma = (math.sqrt(N(i) + g * g) - math.sqrt(N(i))) / alpha
        Z(i) = Z(i) + g - sigma * W(i)
        N(i) = N(i) + g * g
    }

  }

  def predict(data: Vector): Double ={
    var p = 0.0
    p = dot(data, Vectors.dense(W))
    p = 1.0 / (1.0 + math.exp(-p))

    p
  }
}



