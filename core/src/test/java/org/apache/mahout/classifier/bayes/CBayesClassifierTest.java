/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.mahout.classifier.bayes;

import org.apache.mahout.classifier.ClassifierResult;
import org.apache.mahout.classifier.bayes.algorithm.CBayesAlgorithm;
import org.apache.mahout.classifier.bayes.common.BayesParameters;
import org.apache.mahout.classifier.bayes.datastore.InMemoryBayesDatastore;
import org.apache.mahout.classifier.bayes.exceptions.InvalidDatastoreException;
import org.apache.mahout.classifier.bayes.interfaces.Algorithm;
import org.apache.mahout.classifier.bayes.model.ClassifierContext;
import org.apache.mahout.common.MahoutTestCase;

public class CBayesClassifierTest extends MahoutTestCase {
  
  protected Algorithm algorithm;
  protected InMemoryBayesDatastore store;

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    algorithm = new CBayesAlgorithm();
    store = new InMemoryBayesDatastore(new BayesParameters(1));
    // String[] labels = new String[]{"a", "b", "c", "d", "e"};
    // long[] labelCounts = new long[]{6, 20, 60, 100, 200};
    // String[] features = new String[]{"aa", "bb", "cc", "dd", "ee"};
    store.setSigmaJSigmaK(500.0);
    
    store.setSumFeatureWeight("aa", 80);
    store.setSumFeatureWeight("bb", 21);
    store.setSumFeatureWeight("cc", 60);
    store.setSumFeatureWeight("dd", 115);
    store.setSumFeatureWeight("ee", 100);
    
    store.setSumLabelWeight("a", 100);
    store.setSumLabelWeight("b", 100);
    store.setSumLabelWeight("c", 100);
    store.setSumLabelWeight("d", 100);
    store.setSumLabelWeight("e", 100);
    
    store.setThetaNormalizer("a", -100);
    store.setThetaNormalizer("b", -100);
    store.setThetaNormalizer("c", -100);
    store.setThetaNormalizer("d", -100);
    store.setThetaNormalizer("e", -100);
    
    store.loadFeatureWeight("aa", "a", 5);
    store.loadFeatureWeight("bb", "a", 1);
    
    store.loadFeatureWeight("bb", "b", 20);
    
    store.loadFeatureWeight("cc", "c", 30);
    store.loadFeatureWeight("aa", "c", 25);
    store.loadFeatureWeight("dd", "c", 5);
    
    store.loadFeatureWeight("dd", "d", 60);
    store.loadFeatureWeight("cc", "d", 40);
    
    store.loadFeatureWeight("ee", "e", 100);
    store.loadFeatureWeight("aa", "e", 50);
    store.loadFeatureWeight("dd", "e", 50);
    store.updateVocabCount();
  }
  
  public void test() throws InvalidDatastoreException {
    ClassifierContext classifier = new ClassifierContext(algorithm, store);
    String[] document = {"aa", "ff"};
    ClassifierResult result = classifier.classifyDocument(document, "unknown");
    assertNotNull("category is null and it shouldn't be", result);
    assertEquals(result + " is not equal to e", "e", result.getLabel());
    
    document = new String[] {"ff"};
    result = classifier.classifyDocument(document, "unknown");
    assertNotNull("category is null and it shouldn't be", result);
    assertEquals(result + " is not equal to d", "d", result.getLabel());
    
    document = new String[] {"cc"};
    result = classifier.classifyDocument(document, "unknown");
    assertNotNull("category is null and it shouldn't be", result);
    assertEquals(result + " is not equal to d", "d", result.getLabel());
  }
  
  public void testResults() throws Exception {
    ClassifierContext classifier = new ClassifierContext(algorithm, store);
    String[] document = {"aa", "ff"};
    ClassifierResult result = classifier.classifyDocument(document, "unknown");
    assertNotNull("category is null and it shouldn't be", result);
    System.out.println("Result: " + result);
  }
}