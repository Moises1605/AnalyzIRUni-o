package br.uefs.analyzIR.failureAnalysis;

public class GTItem {

    private String name;
    private int revelance;

    public GTItem(String name, int relevance) {
        this.name = name;
        this.revelance = relevance;

    }

    public String getName() {

        return this.name;
    }

    public int getRelevance() {
        return this.revelance;
    }

}
