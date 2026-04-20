package com.network.common;

public class Matrix {
	private double[][] data;
	private int rows;
	private int cols;

	public Matrix(int rows, int cols) {
		this.rows = rows;
		this.cols = cols;
		this.data = new double[rows][cols];
	}

	public Matrix(double[][] data) {
		this.rows = data.length;
		this.cols = data[0].length;
		this.data = new double[rows][cols];
		for (int i = 0; i < rows; i++) {
			System.arraycopy(data[i], 0, this.data[i], 0, cols);
		}
	}

	public double[][] getData() {
		return data;
	}

	public int getRows() {
		return rows;
	}

	public int getCols() {
		return cols;
	}

	// 1. Cong
	public static Matrix cong(Matrix a, Matrix b) {
		Matrix res = new Matrix(a.rows, a.cols);
		for (int i = 0; i < a.rows; i++) {
			for (int j = 0; j < a.cols; j++) {
				res.data[i][j] = a.data[i][j] + b.data[i][j];
			}
		}
		return res;
	}

	// 2. Tru
	public static Matrix tru(Matrix a, Matrix b) {
		Matrix res = new Matrix(a.rows, a.cols);
		for (int i = 0; i < a.rows; i++) {
			for (int j = 0; j < a.cols; j++) {
				res.data[i][j] = a.data[i][j] - b.data[i][j];
			}
		}
		return res;
	}

	// 3. Nhan
	public static Matrix nhan(Matrix a, Matrix b) {
		Matrix res = new Matrix(a.rows, b.cols);
		for (int i = 0; i < a.rows; i++) {
			for (int j = 0; j < b.cols; j++) {
				for (int k = 0; k < a.cols; k++)
					res.data[i][j] += a.data[i][k] * b.data[k][j];
			}
		}
		return res;
	}

	// 4. Scalar
	public static Matrix nhanScalar(Matrix a, double s) {
		Matrix res = new Matrix(a.rows, a.cols);
		for (int i = 0; i < a.rows; i++) {
			for (int j = 0; j < a.cols; j++) {
				res.data[i][j] = a.data[i][j] * s;
			}
		}
		return res;
	}

	// 5. Chuyen vi
	public Matrix chuyenVi() {
		Matrix res = new Matrix(cols, rows);
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				res.data[j][i] = data[i][j];
			}
		}
		return res;
	}

	// 6. Trace
	public double trace() {
		double s = 0;
		for (int i = 0; i < rows; i++) {
			s += data[i][i];
		}
		return s;
	}

	// 7. Doi xung
	public boolean laDoiXung() {
		if (rows != cols) {
			return false;
		}
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < i; j++) {
				if (Math.abs(data[i][j] - data[j][i]) > 1e-10) {
					return false;
				}
			}
		}
		return true;
	}

	// 8. Dinh thuc (Gauss)
	public double dinhThuc() {
		int n = rows;
		double[][] temp = new double[n][n];
		for (int i = 0; i < n; i++) {
			System.arraycopy(data[i], 0, temp[i], 0, n);
		}
		double det = 1.0;
		for (int i = 0; i < n; i++) {
			int pivot = i;
			for (int j = i + 1; j < n; j++) {
				if (Math.abs(temp[j][i]) > Math.abs(temp[pivot][i])) {
					pivot = j;
				}
			}
			double[] tp = temp[i];
			temp[i] = temp[pivot];
			temp[pivot] = tp;

			if (pivot != i) {
				det *= -1;
			}
			if (Math.abs(temp[i][i]) < 1e-10) {
				return 0;
			}
			det *= temp[i][i];
			for (int j = i + 1; j < n; j++) {
				double f = temp[j][i] / temp[i][i];
				for (int k = i + 1; k < n; k++) {
					temp[j][k] -= f * temp[i][k];
				}
			}
		}
		return Math.round(det * 1e10) / 1e10;
	}

	// 9. Nghich dao (Gauss-Jordan)
	public Matrix nghichDao() {
		int n = rows;
		double[][] a = new double[n][n * 2];
		for (int i = 0; i < n; i++) {
			System.arraycopy(data[i], 0, a[i], 0, n);
			a[i][i + n] = 1;
		}
		for (int i = 0; i < n; i++) {
			int p = i;
			for (int j = i + 1; j < n; j++) {
				if (Math.abs(a[j][i]) > Math.abs(a[p][i])) {
					p = j;
				}
			}
			double[] t = a[i];
			a[i] = a[p];
			a[p] = t;
			double div = a[i][i];
			for (int j = i; j < 2 * n; j++) {
				a[i][j] /= div;
			}
			for (int j = 0; j < n; j++) {
				if (i != j) {
					double f = a[j][i];
					for (int k = i; k < 2 * n; k++) {
						a[j][k] -= f * a[i][k];
					}
				}
			}
		}
		double[][] res = new double[n][n];
		for (int i = 0; i < n; i++) {
			System.arraycopy(a[i], n, res[i], 0, n);
		}
		return new Matrix(res);
	}

	// 10. Hang (Rank)
	public int hang() {
		int r = rows, c = cols, rank = 0;
		double[][] mt = new double[r][c];
		for (int i = 0; i < r; i++) {
			System.arraycopy(data[i], 0, mt[i], 0, c);
		}
		boolean[] rowSel = new boolean[r];
		for (int i = 0; i < c; i++) {
			int j;
			for (j = 0; j < r; j++) {
				if (!rowSel[j] && Math.abs(mt[j][i]) > 1e-10) {
					break;
				}
			}
			if (j != r) {
				rank++;
				rowSel[j] = true;
				for (int p = i + 1; p < c; p++) {
					mt[j][p] /= mt[j][i];
				}
				for (int k = 0; k < r; k++) {
					if (k != j && Math.abs(mt[k][i]) > 1e-10) {
						for (int p = i + 1; p < c; p++) {
							mt[k][p] -= mt[k][i] * mt[j][p];
						}
					}
				}
			}
		}
		return rank;
	}
}