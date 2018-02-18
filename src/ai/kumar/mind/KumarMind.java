/**
 *  KumarMind
 *  Copyright 29.06.2016 by Michael Peter Christen, @0rb1t3r
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
 *  
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *  
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program in the file lgpl21.txt
 *  If not, see <http://www.gnu.org/licenses/>.
 */

package ai.kumar.mind;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;

import ai.kumar.DAO;
import ai.kumar.json.JsonTray;
import ai.kumar.mind.KumarMemory.TokenMapList;
import ai.kumar.server.api.kumar.ConsoleService;

public class KumarMind {
    
    public final static int ATTENTION_TIME = 5;
    
    private final Map<String, Set<KumarIntent>> intenttrigger; // a map from a keyword to a set of intents
    private final Map<String, Set<String>> skillexamples; // a map from an skill path to one example
    private final Map<String, String> skillDescriptions; // a map from skill path to description
    private final Map<String, String> skillImage; // a map from skill path to skill image
    private final File[] watchpaths;
    private final File memorypath; // a path where the memory looks for new additions of knowledge with memory files
    private final Map<File, Long> observations; // a mapping of mind memory files to the time when the file was read the last time
    private final KumarReader reader; // responsible to understand written communication
    private final KumarMemory memories; // conversation logs are memories
    private final Map<String, String> skillNames; // a map from skill path to skill names
    private final Map<String, String> authors; // a map from skill path to skill authors
    private final Map<String, String> authorsUrl; // a map from skill path to skill authorsURL
    private final Map<String, String> developerPrivacyPolicies; // a map from skill path to devloper Privacy Policy
    private final Map<String, String> termsOfUse; // a map from skill path to skill termsOfUse
    private final Map<String,Boolean> dynamicContent; // a map from skill path and dynamic Content


    public KumarMind(File memorypath, File... watchpaths) {
        // initialize class objects
        this.watchpaths = watchpaths;
        for (int i = 0; i < watchpaths.length; i++) {
            if (watchpaths[i] != null) watchpaths[i].mkdirs();
        }
        this.memorypath = memorypath;
        if (this.memorypath != null) this.memorypath.mkdirs();
        this.intenttrigger = new ConcurrentHashMap<>();
        this.observations = new HashMap<>();
        this.reader = new KumarReader();
        this.memories = new KumarMemory(memorypath, ATTENTION_TIME);
        this.skillexamples = new TreeMap<>();
        this.skillDescriptions = new TreeMap<>();
        this.skillImage = new TreeMap<>();
        this.authors = new TreeMap<>();
        this.authorsUrl = new TreeMap<>();
        this.skillNames = new TreeMap<>();
        this.developerPrivacyPolicies =new TreeMap<>();
        this.termsOfUse = new TreeMap<>();
        this.dynamicContent = new TreeMap<>();
        // learn all available intents
        try {observe();} catch (IOException e) {
            e.printStackTrace();
        }
    }

    public KumarMemory getMemories() {
        return this.memories;
    }
    
    public KumarReader getReader() {
        return this.reader;
    }
    
    public Map<String, Integer> getUnanswered() {
        return this.memories.getUnanswered();
    }
    
    public List<TokenMapList> unanswered2tokenizedstats() {
        return this.memories.unanswered2tokenizedstats();
    }
    
    public Map<String, Set<String>> getSkillExamples() {
        return this.skillexamples;
    }

    public  Map<String, String> getSkillDescriptions() {
        return this.skillDescriptions;
    }
    public  Map<String, String> getSkillImage() {
        return this.skillImage;
    }

    public Map<String, String> getAuthors() {
        return this.authors;
    }

    public Map<String, String> getAuthorsUrl() {
        return this.authorsUrl;
    }

    public Map<String, String> getDeveloperPrivacyPolicies() {
        return this.developerPrivacyPolicies;
    }

    public Map<String, String> getSkillNames() {
        return this.skillNames;
    }

    public Map<String, String> getTermsOfUse() {
        return this.termsOfUse;
    }

    public Map<String, Boolean> getDynamicContent() {
        return this.dynamicContent;
    }

    public KumarMind observe() throws IOException {
        for (int i = 0; i < watchpaths.length; i++) {
            observe(watchpaths[i]);
        }
        return this;
    }
    
    private void observe(File path) throws IOException {
        if (!path.exists()) return;
        for (File f: path.listFiles()) {
            if (f.isDirectory()) {
                // recursively step into it
                observe(f);
            }
            if (!f.isDirectory() && !f.getName().startsWith(".") && (f.getName().endsWith(".json") || f.getName().endsWith(".txt") || f.getName().endsWith(".aiml"))) {
                if (!observations.containsKey(f) || f.lastModified() > observations.get(f)) {
                    observations.put(f, System.currentTimeMillis());
                    try {
                        JSONObject lesson = new JSONObject();
                        if (f.getName().endsWith(".json")) {
                            lesson = KumarSkill.readJsonSkill(f);
                        }
                        if (f.getName().endsWith(".txt") || f.getName().endsWith(".ezd")) {
                            lesson = KumarSkill.readEzDSkill(new BufferedReader(new FileReader(f)));
                        }
                        if (f.getName().endsWith(".aiml")) {
                            lesson = KumarSkill.readAIMLSkill(f);
                        }
                        learn(lesson, f);
                    } catch (Throwable e) {
                        DAO.severe("BAD JSON FILE: " + f.getAbsolutePath() + ", " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
        }
        
        //this.intenttrigger.forEach((term, map) -> System.out.println("***DEBUG trigger " + term + " -> " + map.toString()));
    }

    
    public KumarMind learn(JSONObject json, File origin) {

        // teach the language parser
        this.reader.learn(json);

        // add console intents
        JSONObject consoleServices = json.has("console") ? json.getJSONObject("console") : new JSONObject();
        consoleServices.keySet().forEach(console -> {
            JSONObject service = consoleServices.getJSONObject(console);
            if (service.has("url") && service.has("path") && service.has("parser")) {
                String url = service.getString("url");
                String path = service.getString("path");
                String parser = service.getString("parser");
                if (parser.equals("json")) {
                    ConsoleService.addGenericConsole(console, url, path);
                }
            }
        });

        // add conversation intents
        final List<Pattern> removalPattern = new ArrayList<>();
        JSONArray intentset = json.has("rules") ? json.getJSONArray("rules") : json.has("intents") ? json.getJSONArray("intents") : new JSONArray();
        intentset.forEach(j -> {
            List<KumarIntent> intents = KumarIntent.getIntents((JSONObject) j, origin);
            intents.forEach(intent -> {
                // add removal pattern
                intent.getKeys().forEach(key -> {
                    Set<KumarIntent> l = this.intenttrigger.get(key);
                    if (l == null) {
                        l = new HashSet<>();
                        this.intenttrigger.put(key, l);
                    }
                    l.add(intent);
                    intent.getPhrases().forEach(phrase -> removalPattern.add(phrase.getPattern()));
                    //intent.getPhrases().forEach(phrase -> this.memories.removeUnanswered(phrase.getPattern()));
                    //System.out.println("***DEBUG: ADD INTENT FOR KEY " + key + ": " + intent.toString());
                });

                // collect intent example and test the intents using the example/expect terms
                if (intent.getExample() != null) {
                    //DAO.log("intent for '" + intent.getExample() + "' in \n" + intent.getSkill() + "\n");
                    Set<String> examples = this.skillexamples.get(intent.getSkill());
                    if (examples == null) {
                        examples = new LinkedHashSet<>();
                        this.skillexamples.put(intent.getSkill(), examples);
                    }
                    examples.add(intent.getExample());
                }
                // skill description
                if(json.has("description"))
                    this.skillDescriptions.put(intent.getSkill(), json.getString("description"));
                // skill image
                if(json.has("image"))
                    this.skillImage.put(intent.getSkill(), json.getString("image"));
                // adding skill meta data
                if(json.has("skill_name"))
                    this.skillNames.put(intent.getSkill(), json.getString("skill_name"));
                if(json.has("author"))
                    this.authors.put(intent.getSkill(), json.getString("author"));
                if(json.has("author_url"))
                    this.authorsUrl.put(intent.getSkill(), json.getString("author_url"));
                if(json.has("developer_privacy_policy"))
                    this.developerPrivacyPolicies.put(intent.getSkill(), json.getString("developer_privacy_policy"));
                if(json.has("terms_of_use"))
                    this.termsOfUse.put(intent.getSkill(), json.getString("terms_of_use"));
                if(json.has("dynamic_content"))
                    this.dynamicContent.put(intent.getSkill(), json.getBoolean("dynamic_content"));

                //if (intent.getExample() != null && intent.getExpect() != null) {}
            });
        });
        
        // finally remove patterns in the memory that are known in a background process
        new Thread(new Runnable() {
            @Override
            public void run() {
                removalPattern.forEach(pattern -> KumarMind.this.memories.removeUnanswered(pattern));
            }
        }).start();
        
        return this;
    }
    
    /**
     * extract the mind system from the intenttrigger
     * @return
     */
    public JSONObject getMind() {
        JSONObject mind = new JSONObject(true);
        this.intenttrigger.forEach((key, intentmap) -> {
            JSONArray intents = new JSONArray();
            mind.put(key, intents);
            intentmap.forEach(intent -> {
                JSONObject r = new JSONObject(true);
                r.putAll(intent.toJSON());
                r.put("hash", intent.hashCode());
                intents.put(r);
            });
        });
        return mind;
    }
    
    /**
     * This is the core principle of creativity: being able to match a given input
     * with problem-solving knowledge.
     * This method finds ideas (with a query instantiated intents) for a given query.
     * The intents are selected using a scoring system and pattern matching with the query.
     * Not only the most recent user query is considered for intent selection but also
     * previously requested queries and their answers to be able to set new intent selections
     * in the context of the previous conversation.
     * @param query the user input
     * @param previous_argument the latest conversation with the same user
     * @param maxcount the maximum number of ideas to return
     * @return an ordered list of ideas, first idea should be considered first.
     */
    public List<KumarIdea> creativity(String query, KumarLanguage userLanguage, KumarThought latest_thought, int maxcount) {
        // tokenize query to have hint for idea collection
        final List<KumarIdea> ideas = new ArrayList<>();
        this.reader.tokenizeSentence(query).forEach(token -> {
            Set<KumarIntent> intent_for_category = this.intenttrigger.get(token.categorized);
            Set<KumarIntent> intent_for_original = token.original.equals(token.categorized) ? null : this.intenttrigger.get(token.original);
            Set<KumarIntent> r = new HashSet<>();
            if (intent_for_category != null) r.addAll(intent_for_category);
            if (intent_for_original != null) r.addAll(intent_for_original);
            r.forEach(intent -> ideas.add(new KumarIdea(intent).setToken(token)));
        });
        
        for (KumarIdea idea: ideas) DAO.log("idea.phrase-1: score=" + idea.getIntent().getScore(userLanguage).score + " : " + idea.getIntent().getPhrases().toString() + " " + idea.getIntent().getActionsClone());
        
        // add catchall intents always (those are the 'bad ideas')
        Collection<KumarIntent> ca = this.intenttrigger.get(KumarIntent.CATCHALL_KEY);
        if (ca != null) ca.forEach(intent -> ideas.add(new KumarIdea(intent)));
        
        // create list of all ideas that might apply
        TreeMap<Long, List<KumarIdea>> scored = new TreeMap<>();
        AtomicLong count = new AtomicLong(0);
        ideas.forEach(idea -> {
            int score = idea.getIntent().getScore(userLanguage).score;
            long orderkey = Long.MAX_VALUE - ((long) score) * 1000L + count.incrementAndGet();
            List<KumarIdea> r = scored.get(orderkey);
            if (r == null) {r = new ArrayList<>(); scored.put(orderkey, r);}
            r.add(idea);
        });

        // make a sorted list of all ideas
        ideas.clear(); scored.values().forEach(r -> ideas.addAll(r));
        
        for (KumarIdea idea: ideas) DAO.log("idea.phrase-2: score=" + idea.getIntent().getScore(userLanguage).score + " : " + idea.getIntent().getPhrases().toString() + " " + idea.getIntent().getActionsClone());
        
        // test ideas and collect those which match up to maxcount
        List<KumarIdea> plausibleIdeas = new ArrayList<>(Math.min(10, maxcount));
        for (KumarIdea idea: ideas) {
            KumarIntent intent = idea.getIntent();
            Collection<Matcher> m = intent.matcher(query);
            if (m.isEmpty()) continue;
            // TODO: evaluate leading SEE flow commands right here as well
            plausibleIdeas.add(idea);
            if (plausibleIdeas.size() >= maxcount) break;
        }

        for (KumarIdea idea: plausibleIdeas) {
            DAO.log("idea.phrase-3: score=" + idea.getIntent().getScore(userLanguage).score + " : " + idea.getIntent().getPhrases().toString() + " " + idea.getIntent().getActionsClone());
            DAO.log("idea.phrase-3:   log=" + idea.getIntent().getScore(userLanguage).log );
        }

        return plausibleIdeas;
    }
    
    /**
     * react on a user input: this causes the selection of deduction intents and the evaluation of the process steps
     * in every intent up to the moment where enough intents have been applied as consideration. The reaction may also
     * cause the evaluation of operational steps which may cause learning effects within the KumarMind.
     * @param query the user input
     * @param maxcount the maximum number of answers (typical is only one)
     * @param client authentication string of the user
     * @param observation an initial thought - that is what kumar experiences in the context. I.e. location and language of the user
     * @return
     */
    public List<KumarArgument> react(String query, KumarLanguage userLanguage, int maxcount, String client, KumarThought observation) {
        // get the history a list of thoughts
        KumarArgument observation_argument = new KumarArgument();
        if (observation != null && observation.length() > 0) observation_argument.think(observation);
        List<KumarCognition> cognitions = this.memories.getCognitions(client);
        // latest cognition is first in list
        cognitions.forEach(cognition -> observation_argument.think(cognition.recallDispute()));
        // perform a mindmeld to create a single thought out of the recalled argument
        // the mindmeld will squash the latest thoughts into one so it does not pile up to exponential growth
        KumarThought recall = observation_argument.mindmeld(false);
        
        // normalize the query
        query = KumarPhrase.normalizeExpression(query);
        
        // find an answer
        List<KumarArgument> answers = new ArrayList<>();
        List<KumarIdea> ideas = creativity(query, userLanguage, recall, 100); // create a list of ideas which are possible intents

        // test all ideas: the ideas are ranked in such a way that the best one is considered first
        ideatest: for (KumarIdea idea: ideas) {
            // compute an argument: because one intent represents a horn clause, the argument is a deduction track, a "proof" of the result.
            KumarArgument argument = idea.getIntent().consideration(query, recall, idea.getToken(), this, client);

            // arguments may fail; a failed proof is one which does not exist. Therefore an argument may be empty
            if (argument == null) continue ideatest; // consider only sound arguments
            answers.add(argument); // a valid idea
            if (answers.size() >= maxcount) break; // and stop if we are done
        }
        return answers;
    }
    
    public class Reaction {
        private String expression;
        private KumarThought mindstate;
        
        public Reaction(String query, KumarLanguage userLanguage, String client, KumarThought observation) throws RuntimeException {
            List<KumarArgument> datalist = react(query, userLanguage, 1, client, observation);
            if (datalist.size() == 0) throw new RuntimeException("datalist is empty");
            KumarArgument bestargument = datalist.get(0);
            if (bestargument.getActions().isEmpty()) throw new RuntimeException("action list is empty");
            KumarAction action = bestargument.getActions().get(0);
            this.expression = action.execution(bestargument, KumarMind.this, client, userLanguage).getStringAttr("expression");
            this.mindstate = bestargument.mindstate();
            //KumarThought mindmeld = bestargument.mindmeld(true);
        }
        
        public String getExpression() {
            return this.expression;
        }
        
        public KumarThought getMindstate() {
            return this.mindstate;
        }
        
        public String toString() {
            return this.getExpression();
        }
    }

    public Set<String> getIntentsetNames(String client) {
        return this.memories.getIntentsetNames(client);
    }
    
    public JsonTray getIntentset(String client, String name) throws IOException {
        return this.memories.getIntentset(client, name);
    }
    
    public static void main(String[] args) {
        try {
            File init = new File(new File("conf"), "kumar");
            File watch = new File(new File("data"), "kumar");
            KumarMind mem = new KumarMind(watch, init, watch);
            File file = new File("conf/kumar/kumar_cognition_000.json");
            JSONObject lesson = KumarSkill.readJsonSkill(file);
            mem.learn(lesson, file);
            System.out.println(mem.new Reaction("I feel funny", KumarLanguage.unknown, "localhost", new KumarThought()).getExpression());
            System.out.println(mem.new Reaction("Help me!", KumarLanguage.unknown, "localhost", new KumarThought()).getExpression());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    
}
