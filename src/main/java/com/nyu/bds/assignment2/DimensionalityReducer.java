package com.nyu.bds.assignment2;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

import Jama.EigenvalueDecomposition;
import Jama.Matrix;
import Jama.SingularValueDecomposition;

public class DimensionalityReducer {
	Matrix tfidf;

	public DimensionalityReducer(Matrix tfidf) {
		this.tfidf = tfidf;
	}

	public Matrix performSvd(int outputDims) {
		System.out.println("\nPerforming SVD");
		Matrix input = tfidf.times(tfidf.transpose());
		SingularValueDecomposition svd = new SingularValueDecomposition(input);
		Matrix concepts = input.times(svd.getS().getMatrix(0, 23, 0, 1)).transpose();
		System.out.println("Singular values:" + Arrays.toString(svd.getSingularValues()));
		return concepts;
	}

	public Matrix performPca(int outputDims) {
		System.out.println("\nPerforming PCA");
		Matrix input = tfidf.times(tfidf.transpose());
		EigenvalueDecomposition pca = new EigenvalueDecomposition(input);
		Matrix concepts = input.times(pca.getV().getMatrix(0, 23, 22, 23)).transpose();
		System.out.println("Eigen values:" + Arrays.toString(pca.getRealEigenvalues()));
		return concepts;
	}

}
