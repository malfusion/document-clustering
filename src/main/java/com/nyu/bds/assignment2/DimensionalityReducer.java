package com.nyu.bds.assignment2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

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
		System.out.println("Top 5 Singular values:" + Arrays.toString(Arrays.copyOfRange(svd.getSingularValues(), 0, 5)));
		return concepts;
	}

	public Matrix performPca(int outputDims) {
		System.out.println("\nPerforming PCA");
		Matrix input = tfidf.times(tfidf.transpose());
		EigenvalueDecomposition pca = new EigenvalueDecomposition(input);
		Matrix concepts = input.times(pca.getV().getMatrix(0, 23, 22, 23)).transpose();
		System.out.println("Top 5 Eigen values:" + Arrays.toString(Arrays.copyOfRange(pca.getRealEigenvalues(), 19, 24)));
		return concepts;
	}

}
