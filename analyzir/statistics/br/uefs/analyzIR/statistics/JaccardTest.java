/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uefs.analyzIR.statistics;

import java.util.LinkedList;

/** Calculate Jaccard index for two vectors
 *
 * @author KHAICK O. BRITO
 */
public class JaccardTest {
    
    /** Calculates jaccard index for two vectors
     *
     * @param vector1 Vector '1' cointaining string elements
     * @param vector2 Vector '2' cointaining string elements
     * @return Return Jaccard index
     */
    public double calculateIndex(String[] vector1, String[] vector2){
        //Calculate the number of intersections
        double intersecValue = intersecCalc(vector1, vector2);
        //Calculate the size of the union
        double unionValue = unionCalc(vector1,vector2,intersecValue);
        //Calculate Jaccard Index by the formula:
        // |IntersectionValue| / |UnionValue|
        double jaccardIndex = intersecValue/unionValue;
        //Returns the index found
        return jaccardIndex;
    }
    
    /** Calculates the number of elements that intersect among two vectors
     * 
     * @param vector1 Vector '1' cointaining string elements
     * @param vector2 Vector '2' cointaining string elements
     * @return Number of elements that intersect
     */
    private double intersecCalc(String[] vector1, String[] vector2) {
        double intersecCounter = 0;
        
        //Compares each element of the first vector with each of the second
        for (int i = 0; i < vector1.length; i++) {
            String vector1Element = vector1[i];
            for (int j = 0; j < vector2.length; j++) {
                String vector2Element = vector2[j];
                if(vector1Element.equals(vector2Element)){
                    //Counts everytime and element of the first vector intersect
                    //with one of the second
                    intersecCounter++; 
                    break;
                }
            }
        }
        //return the number of elements that intersected
        return intersecCounter;
    }
    
    /** Calculates the union of elements among two vectors
     * 
     * @param vector1 Vector '1' cointaining double elements
     * @param vector2 Vector '2' cointaining double elements
     * @param intersecValue Number of elements that intersects among two vectors
     * @return Number of elements in an union among '1' and '2'
     */
    private double unionCalc(String[] vector1, String[] vector2, double intersecValue) {
        //Gets the size of the vectors
        int vector1Size = vector1.length;
        int vector2Size = vector2.length;
        //A union can be calculated by this formula: 
        // |number of elements of '1'|  + |number of elements of '2'| - |Intersect value for '1' and '2'|
        double unionValue = (vector1Size + vector2Size)-intersecValue;
        return unionValue;
    }
}
