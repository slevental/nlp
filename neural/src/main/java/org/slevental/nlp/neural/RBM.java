package org.slevental.nlp.neural;

import com.google.common.collect.Table;

import java.util.Arrays;
import java.util.Random;

/**
 * Implementation of restricted boltzmann machine
 */
public class RBM implements Trainer {
    private final static Random rnd = new Random();
    private final static double standardDeviation = 0.1;

    private final int visibleCount;
    private final int hiddenCount;
    private final double learningRate;
    private final int epochs;

    private final double[][] weights;

    public RBM(int visibleCount, int hiddenCount, double learningRate, int epochs) {
        this.visibleCount = visibleCount;
        this.hiddenCount = hiddenCount;
        this.learningRate = learningRate;
        this.epochs = epochs;
        this.weights = new double[visibleCount][hiddenCount];
        initWithRandom(rnd, weights, standardDeviation);
    }

    @Override
    public Model train(Table table){

        return null;
    }

    protected static void fillRows(double[][] array, int idx, double value){
        Arrays.fill(array[idx], value);
    }

    protected static void fillColumns(double[][] array, int idx, double value){
        for (int i = 0; i < array.length; i++) {
            array[i][idx] = value;
        }
    }

    /*

 def train(self, data, max_epochs = 1000):
    """
    Train the machine.

    Parameters
    ----------
    data: A matrix where each row is a training example consisting of the states of visible units.
    """

    num_examples = data.shape[0]

    # Insert bias units of 1 into the first column.
    data = np.insert(data, 0, 1, axis = 1)

    for epoch in range(max_epochs):
      # Clamp to the data and sample from the hidden units.
      # (This is the "positive CD phase", aka the reality phase.)
      pos_hidden_activations = np.dot(data, self.weights)
      pos_hidden_probs = self._logistic(pos_hidden_activations)
      pos_hidden_states = pos_hidden_probs > np.random.rand(num_examples, self.num_hidden + 1)
      # Note that we're using the activation *probabilities* of the hidden states, not the hidden states
      # themselves, when computing associations. We could also use the states; see section 3 of Hinton's
      # "A Practical Guide to Training Restricted Boltzmann Machines" for more.
      pos_associations = np.dot(data.T, pos_hidden_probs)

      # Reconstruct the visible units and sample again from the hidden units.
      # (This is the "negative CD phase", aka the daydreaming phase.)
      neg_visible_activations = np.dot(pos_hidden_states, self.weights.T)
      neg_visible_probs = self._logistic(neg_visible_activations)
      neg_visible_probs[:,0] = 1 # Fix the bias unit.
      neg_hidden_activations = np.dot(neg_visible_probs, self.weights)
      neg_hidden_probs = self._logistic(neg_hidden_activations)
      # Note, again, that we're using the activation *probabilities* when computing associations, not the states
      # themselves.
      neg_associations = np.dot(neg_visible_probs.T, neg_hidden_probs)

      # Update weights.
      self.weights += self.learning_rate * ((pos_associations - neg_associations) / num_examples)

      error = np.sum((data - neg_visible_probs) ** 2)
      print "Epoch %s: error is %s" % (epoch, error)

     */

    /**
     * Init array with random values, using provided generator,
     * bias weight left filled with zeros
     *
     * @param rnd generator
     * @param weights matrix to fill
     * @param sd standard deviation
     */
    private static void initWithRandom(Random rnd, double[][] weights, double sd) {
        for (int i = 1; i < weights.length; i++) {
            double[] weight = weights[i];
            for (int j = 1; j < weight.length; j++) {
                weight[j] = rnd.nextDouble() * sd;
            }
        }
    }
}
