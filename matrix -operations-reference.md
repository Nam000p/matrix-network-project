# Matrix Operations Reference

## 1. Input Data
### Matrix A (3x3, Rank 3, Invertible)
$$A = \begin{bmatrix} 1 & 2 & 3 \\ 0 & 1 & 4 \\ 5 & 6 & 0 \end{bmatrix}$$

### Matrix B (3x3)
$$B = \begin{bmatrix} 7 & 8 & 9 \\ 1 & 2 & 0 \\ 3 & 4 & 5 \end{bmatrix}$$

### Scalar k
$$k = 2$$

### Symmetric Matrix S
$$S = \begin{bmatrix} 1 & 7 & 3 \\ 7 & 4 & -5 \\ 3 & -5 & 6 \end{bmatrix}$$

---

## 2. Operation Results

### 2.1. Addition ($A + B$)
$$A + B = \begin{bmatrix} 8 & 10 & 12 \\ 1 & 3 & 4 \\ 8 & 10 & 5 \end{bmatrix}$$

### 2.2. Subtraction ($A - B$)
$$A - B = \begin{bmatrix} -6 & -6 & -6 \\ -1 & -1 & 4 \\ 2 & 2 & -5 \end{bmatrix}$$

### 2.3. Multiplication ($A \times B$)
$$A \times B = \begin{bmatrix} 18 & 24 & 24 \\ 13 & 18 & 20 \\ 41 & 52 & 45 \end{bmatrix}$$

### 2.4. Scalar Multiplication ($k \cdot A$)
$$2A = \begin{bmatrix} 2 & 4 & 6 \\ 0 & 2 & 8 \\ 10 & 12 & 0 \end{bmatrix}$$

### 2.5. Transpose ($A^T$)
$$A^T = \begin{bmatrix} 1 & 0 & 5 \\ 2 & 1 & 6 \\ 3 & 4 & 0 \end{bmatrix}$$

### 2.6. Trace ($tr(A)$)
$$tr(A) = 1 + 1 + 0 = 2$$

### 2.7. Symmetry Check ($S = S^T$)
The matrix $S$ is symmetric because $S_{ij} = S_{ji}$ for all $i, j$.
$$S^T = \begin{bmatrix} 1 & 7 & 3 \\ 7 & 4 & -5 \\ 3 & -5 & 6 \end{bmatrix} = S$$

### 2.8. Determinant ($det(A)$)
$$|A| = 1(0 - 24) - 2(0 - 20) + 3(0 - 5) = -24 + 40 - 15 = 1$$

### 2.9. Inverse ($A^{-1}$)
Since $det(A) = 1 \neq 0$, the inverse exists:
$$A^{-1} = \begin{bmatrix} -24 & 18 & 5 \\ 20 & -15 & -4 \\ -5 & 4 & 1 \end{bmatrix}$$

### 2.10. Rank ($rank(A)$)
$$rank(A) = 3$$