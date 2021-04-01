package app.test.migrator.matching;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import app.test.migrator.matching.server.ObjectSender;
import app.test.migrator.matching.server.ScoredObject;
import app.test.migrator.matching.server.ServerSemanticMatchingPairs;
import app.test.migrator.matching.util.Event;
import app.test.migrator.matching.util.Pair;
import app.test.migrator.matching.util.State;
import app.test.migrator.matching.util.Transition;
import app.test.migrator.matching.util.uiautomator.BasicTreeNode;
import app.test.migrator.matching.util.uiautomator.UiHierarchyXmlLoader;
import app.test.migrator.matching.util.uiautomator.UiNode;

public class CommonMatchingOps {

    private static final List<String> labels =
            Arrays.asList("TextView", "DialogTitle", "ImageView", "IconTextView", "TextInputLayout");
    private static final List<String> complex =
            Arrays.asList("android.support.v7.widget.RecyclerView",
                    "android.support.v7.widget.RecyclerView",
                    "android.widget.ExpandableListView",
                    "android.widget.ListView",
                    "RelativeLayout",
                    "LinearLayout",
                    "FrameLayout",
                    "Spinner");


    static List<ScoredObject<Pair<Event, List<Double>>>> getDynamicCandidates(State currState, UiNode node) throws IOException {
        List<Pair<Event, List<Double>>> dynamicPairList = currState.getActionables();
        addActivityNameToEvents(dynamicPairList, currState);
        UiNode root = getRoot(currState);
        List<Pair<Event, List<Double>>> labels = findLabels(root, new ArrayList<Pair<Event, List<Double>>>());
        return new ServerSemanticMatchingPairs(dynamicPairList, labels, node, null).getScoredObjects();
    }

    static List<ScoredObject<Pair<Event, List<Double>>>> getDynamicCandidates(State currState, Transition transition) throws IOException {
        List<Pair<Event, List<Double>>> dynamicPairList = currState.getActionables();
        addActivityNameToEvents(dynamicPairList, currState);
        State sourceState = transition.getFrom();
        UiNode sourceNode = transition.getLabel().first.getTargetElement();
        ObjectSender.sendException(new Exception(sourceNode.getAttributes().toString()));
        List<Pair<Event, List<Double>>> targetLabels = findLabels(currState, new ArrayList<Pair<Event, List<Double>>>());
        List<Pair<Event, List<Double>>> sourceLabels = findLabels(sourceState, new ArrayList<Pair<Event, List<Double>>>());
        return new ServerSemanticMatchingPairs(dynamicPairList, targetLabels, sourceNode, sourceLabels).getScoredObjects();
    }


    private static void addActivityNameToEvents(List<Pair<Event, List<Double>>> dynamicPairList, State currState) {
        String activityName = currState.getFileName();
        dynamicPairList.forEach(x ->
                x.first.getTargetElement().addAtrribute("activity", activityName));
    }

    static List<Pair<Event, List<Double>>> findLabels(State state, List<Pair<Event, List<Double>>> eventPairs) {
        UiNode root = getRoot(state);
        return findLabels(root, eventPairs);
    }

    private static List<Pair<Event, List<Double>>> findLabels(UiNode root, List<Pair<Event, List<Double>>> eventPairs) {
        if (root == null) return new ArrayList<Pair<Event, List<Double>>>();

        BasicTreeNode[] nodes = root.getChildren();
        for (BasicTreeNode node1 : nodes) {
            UiNode node = (UiNode) node1;

            String id = node.getAttribute("resource-id");
            String type = node.getAttribute("class");


            if (id.contains("com.android.systemui") || id.contains("statusBarBackground") || id.contains("navigationBarBackground"))
                continue;
            if (type.contains("EditText"))
                continue;

            Event testRecorderEvent = null;

            if (isComplexNode(type) && node.getChildCount() > 0) {
                List<Pair<UiNode, Boolean>> leafNodes = new ArrayList<>();
                findLeafNodes(node, leafNodes, false);

                for (Pair<UiNode, Boolean> leaf : leafNodes) {
                    String leafType = leaf.first.getAttribute("class");
                    if (type.equals("android.support.v7.widget.RecyclerView") || type.equals("android.widget.ListView")) {
                        continue;
                    } else {
                        if (isNotClickable(leaf.first))
                            testRecorderEvent = new Event("NONE", leaf.first, "", "0");
                    }

                    if (testRecorderEvent != null && !alreadyContainsClickable(testRecorderEvent, eventPairs)) {
                        List<UiNode> webkitNodes = new ArrayList<>();
                        findWebkitAncestors(testRecorderEvent.getTargetElement(), webkitNodes);
                        if (webkitNodes.size() < 1 && isLabelNode(leaf.first)) {
                            List<Double> indexes = new ArrayList<>();
                            eventPairs.add(new Pair<>(testRecorderEvent, indexes));
                        }
                    }
                }
            } else {
                if (isNotClickable(node))
                    testRecorderEvent = new Event("NONE", node, "", "0");
            }

            if (testRecorderEvent != null && !alreadyContainsClickable(testRecorderEvent, eventPairs)) {
                List<Double> indexes = new ArrayList<>();
                List<UiNode> webkitNodes = new ArrayList<>();
                findWebkitAncestors(testRecorderEvent.getTargetElement(), webkitNodes);
                if (webkitNodes.size() < 1 && isLabelNode(node)) {
                    eventPairs.add(new Pair<>(testRecorderEvent, indexes));
                }
            }
            findLabels(node, eventPairs);
        }
        return eventPairs;
    }

    private static boolean isNotClickable(UiNode node) {
        String clickable = node.getAttribute("clickable");
        String long_clickable = node.getAttribute("long-clickable");
        String checkable = node.getAttribute("checkable");
        String type = node.getAttribute("class");
        if (type.endsWith("Button"))
            return false;

        return !(long_clickable.equals("true") || clickable.equals("true") || checkable.equals("true"));
    }

    static void findWebkitAncestors(UiNode node, List<UiNode> webkitNodes) {
        while (node != null) {
            if (node.getAttribute("class") != null && node.getAttribute("class").equals("android.webkit.WebView"))
                webkitNodes.add(node);

            node = (UiNode) node.getParent();
        }
    }

    static boolean alreadyContainsClickable(Event event, List<Pair<Event, List<Double>>> clickables) {
        for (Pair<Event, List<Double>> c : clickables) {
            UiNode targetElement = c.first.getTargetElement();
            UiNode eventTargetElement = event.getTargetElement();
            if (targetElement == null || eventTargetElement == null) continue;
            if (targetElement.toString().equals(eventTargetElement.toString())) return true;
        }

        return false;
    }

    static void findLeafNodes(UiNode node, List<Pair<UiNode, Boolean>> leafNodes, Boolean longClickable) {
        if (node.getChildCount() == 0 && !node.getAttribute("class").equals("android.support.v7.widget.RecyclerView"))
            leafNodes.add(new Pair<>(node, longClickable));

        for (BasicTreeNode leafNode : node.getChildren()) {
            if (((UiNode) leafNode).getAttribute("long-clickable").equals("true"))
                longClickable = true;

            findLeafNodes((UiNode) leafNode, leafNodes, longClickable);
        }
    }

    private static boolean typeEndsWithOneOf(final String type, final List<String> types) {

        for (final String t : types) {
            if (type.endsWith(t)) {
                return true;
            }
        }
        return false;
    }

    private static boolean typeContainsOneOf(final String type, final List<String> types) {

        for (final String t : types) {
            if (type.contains(t)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isComplexNode(String type) {
        return typeContainsOneOf(type, complex);
    }

    private static boolean isLabelNode(UiNode node) {
        String type = node.getAttribute("class");
        if (!node.getAttribute("text").equals("") ||
                !node.getAttribute("content-desc").equals(""))
            return typeEndsWithOneOf(type, labels);
        return false;
    }

    private static String getText(UiNode node) {
        String text = node.getAttribute("text");
        if (!text.equals(""))
            return text;
        return node.getAttribute("content-desc");
    }

    public static void addFamilyAttributes(UiNode node) throws IOException {
        UiNode parent = (UiNode) node.getParent();
        if (parent == null)
            return;
        String parentText = parent.getAttribute("text");
        String nodeText = node.getAttribute("text");
        List<BasicTreeNode> childrenList = parent.getChildrenList();
        UiNode sibling = null;
        for (BasicTreeNode child : childrenList) {
            sibling = (UiNode) child;
            String siblingText = node.getAttribute("text");
            if (!siblingText.equals(nodeText))
                break;
            sibling = null;
        }
        if (sibling != null) {
            String siblingText = sibling.getAttribute("text");
            node.addAtrribute("sibling_text", siblingText);
        }
        node.addAtrribute("parent_text", parentText);
    }


    private static UiNode getRoot(State state) {
        UiHierarchyXmlLoader xmlLoader = new UiHierarchyXmlLoader();
        return (UiNode) xmlLoader.parseXml(state.getGUIHierarchy());
    }

}
