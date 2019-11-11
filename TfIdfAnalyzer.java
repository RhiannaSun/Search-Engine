package search.analyzers;

import datastructures.concrete.ChainedHashSet;
import datastructures.concrete.dictionaries.ChainedHashDictionary;
import datastructures.concrete.dictionaries.KVPair;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.IList;
import datastructures.interfaces.ISet;
import search.models.Webpage;
import java.net.URI;

/**
 * This class is responsible for computing how "relevant" any given document is
 * to a given search query.
 *
 * See the spec for more details.
 */
public class TfIdfAnalyzer {
    // This field must contain the IDF score for every single word in all
    // the documents.
    private IDictionary<String, Double> idfScores;

    // This field must contain the TF-IDF vector for each webpage you were given
    // in the constructor.
    //
    // We will use each webpage's page URI as a unique key.
    private IDictionary<URI, IDictionary<String, Double>> documentTfIdfVectors;

    // Feel free to add extra fields and helper methods.

    private IDictionary<URI, Double> documentNorms;

    /**
     * @param webpages  A set of all webpages we have parsed. Must be non-null and
     *                  must not contain nulls.
     */
    public TfIdfAnalyzer(ISet<Webpage> webpages) {
        // Implementation note: We have commented these method calls out so your
        // search engine doesn't immediately crash when you try running it for the
        // first time.
        //

        this.idfScores = this.computeIdfScores(webpages);
        this.documentTfIdfVectors = this.computeAllDocumentTfIdfVectors(webpages);
        this.documentNorms = this.computeNorms(documentTfIdfVectors);
    }

    // Note: this method, strictly speaking, doesn't need to exist. However,
    // we've included it so we can add some unit tests to help verify that your
    // constructor correctly initializes your fields.
    public IDictionary<URI, IDictionary<String, Double>> getDocumentTfIdfVectors() {
        return this.documentTfIdfVectors;
    }

    // Note: these private methods are suggestions or hints on how to structure your
    // code. However, since they're private, you're not obligated to implement exactly
    // these methods: feel free to change or modify these methods however you want. The
    // important thing is that your 'computeRelevance' method ultimately returns the
    // correct answer in an efficient manner.

    /**
     * Return a dictionary mapping every single unique word found
     * in every single document to their IDF score.
     */

    private IDictionary<String, Double> computeIdfScores(ISet<Webpage> pages) {
        IDictionary<String, Double> termInDocsCount = new ChainedHashDictionary<>();
        for (Webpage page: pages) {
            IList<String> wordsInPage = page.getWords();
            ISet<String> uniqueWords = new ChainedHashSet<>();
            for (String word: wordsInPage) {
                if (!uniqueWords.contains(word)) {
                    uniqueWords.add(word);  // record the page has the word
                    termInDocsCount.put(word, termInDocsCount.getOrDefault(word, 0.0) + 1.0);
                }
            }
        }
        int totalDoc = pages.size();
        IDictionary<String, Double> idf = new ChainedHashDictionary<>();
        for (KVPair<String, Double> word : termInDocsCount) {
            double idfScore = Math.log(totalDoc/word.getValue());
            idf.put(word.getKey(), idfScore);
        }
        return idf;
    }

    /**
     * Returns a dictionary mapping every unique word found in the given list
     * to their term frequency (TF) score.
     *
     * The input list represents the words contained within a single document.
     */
    private IDictionary<String, Double> computeTfScores(IList<String> words) {
        double totalWords = words.size();
        IDictionary<String, Double> count = new ChainedHashDictionary<>();
        for (String word: words) {
            if (count.containsKey(word)) {
                double currentCount = count.get(word);
                currentCount++;
                count.put(word, currentCount);
            } else {
                count.put(word, 1.0);
            }
        }
        IDictionary<String, Double> tfScores = new ChainedHashDictionary<>();
        for (KVPair<String, Double> word : count) {
            double tfScore = word.getValue()/totalWords;
            tfScores.put(word.getKey(), tfScore);
        }
        return tfScores;
    }

    /**
     * See spec for more details on what this method should do.
     */
    private IDictionary<URI, IDictionary<String, Double>> computeAllDocumentTfIdfVectors(ISet<Webpage> pages) {
        // Hint: this method should use the idfScores field and
        // call the computeTfScores(...) method.
        IDictionary<URI, IDictionary<String, Double>> vector = new ChainedHashDictionary<>();
        idfScores = computeIdfScores(pages);
        for (Webpage page: pages) {
            URI uri = page.getUri();
            IList<String> words = page.getWords();
            vector.put(uri, computeSingleDocumentTfIdfVectors(words));
        }
        return vector;
    }

    private IDictionary<String, Double> computeSingleDocumentTfIdfVectors(IList<String> list) {
        IDictionary<String, Double> tfIdfScores = new ChainedHashDictionary<>();
        IDictionary<String, Double> tf = computeTfScores(list);
        for (KVPair<String, Double> wordFre: tf) {
            String word = wordFre.getKey();
            if (idfScores.containsKey(word)) {
                Double fre = wordFre.getValue();
                tfIdfScores.put(word, fre * idfScores.get(word));
            }
        }
        return tfIdfScores;
    }
    /**
     * Returns the cosine similarity between the TF-IDF vector for the given query and the
     * URI's document.
     *
     * Precondition: the given uri must have been one of the uris within the list of
     *               webpages given to the constructor.
     */
    public Double computeRelevance(IList<String> query, URI pageUri) {
        // Note: The pseudocode we gave you is not very efficient. When implementing,
        // this method, you should:
        //
        // 1. Figure out what information can be precomputed in your constructor.
        //    Add a third field containing that information.
        //
        // 2. See if you can combine or merge one or more loops.
        if (documentTfIdfVectors.containsKey(pageUri)) {
            IDictionary<String, Double> docVector = documentTfIdfVectors.get(pageUri);
            IDictionary<String, Double> queryVector = computeSingleDocumentTfIdfVectors(query);

            double numerator = 0.0;
            for (KVPair<String, Double> pair : queryVector) {
                String key = pair.getKey();
                numerator += docVector.getOrDefault(key, 0.0) * queryVector.get(key);
            }
            double denominator = documentNorms.get(pageUri) * norm(queryVector);
            if (denominator != 0) {
                return numerator / denominator;
            }
        }
        return 0.0;

    }
    private IDictionary<URI, Double> computeNorms(IDictionary<URI, IDictionary<String, Double>> vectors) {
        IDictionary<URI, Double> norms = new ChainedHashDictionary<>();
        for (KVPair<URI, IDictionary<String, Double>> vector : vectors) {
            URI uri = vector.getKey();
            double norm = norm(vector.getValue());
            norms.put(uri, norm);
        }
        return norms;
    }

    private Double norm(IDictionary<String, Double> vector) {
        double output = 0.0;
        for (KVPair<String, Double> pair: vector) {
            double score = pair.getValue();
            output += score * score;
        }
        return Math.sqrt(output);
    }
}
