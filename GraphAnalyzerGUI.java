import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.algorithm.Dijkstra;
//import org.graphstream.ui.view.DefaultView;
import org.graphstream.ui.view.Viewer;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Iterator;
import org.graphstream.graph.*;

//Muhammad Afeef Imran Mughal (31705)
//Aarij Muhammad Jan (32049)    
//Rafay Ahmed (32077)   


public class GraphAnalyzerGUI extends JFrame {

    private JTextField filePathTextField;
    private JLabel efficiencyLabel;
    private JLabel pathLengthLabel;
    private JPanel graphViewPanel;
    private Viewer viewer;
    private Graph graph;

    public GraphAnalyzerGUI() {
        super("Graph Analyzer");

        // Create GUI components
        JPanel mainPanel = new JPanel(new BorderLayout());

        filePathTextField = new JTextField(20);
        JButton loadButton = new JButton("Load Network");
        loadButton.addActionListener(e -> loadAndAnalyzeGraph());

        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Network File: "));
        inputPanel.add(filePathTextField);
        inputPanel.add(loadButton);

        JPanel outputPanel = new JPanel();
        efficiencyLabel = new JLabel("Network Efficiency (E): ");
        pathLengthLabel = new JLabel("Weighted Average Path Length (L): ");
        outputPanel.add(efficiencyLabel);
        outputPanel.add(pathLengthLabel);

        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(outputPanel, BorderLayout.CENTER);

        // Create the graph view panel
        graphViewPanel = new JPanel(new BorderLayout());
        mainPanel.add(graphViewPanel, BorderLayout.SOUTH);

        // Add the main panel to the frame
        add(mainPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void loadAndAnalyzeGraph() {
        String filePath = filePathTextField.getText();
        graph = loadGraphFromFile(filePath);

        if (graph != null) {
            // Calculate and display E and L
            double networkEfficiency = calculateNetworkEfficiency(graph);
            double weightedAveragePathLength = calculateWeightedAveragePathLength(graph);

            efficiencyLabel.setText("Network Efficiency (E): " + networkEfficiency);
            pathLengthLabel.setText("Weighted Average Path Length (L): " + weightedAveragePathLength);

            // Display the graph
            displayGraph(graph);
        } else {
            JOptionPane.showMessageDialog(this, "Error loading the graph from the file.");
        }
    }

    private Graph loadGraphFromFile(String filePath) {
        Graph graph = new SingleGraph("NetworkGraph");
        try {
            try
            {
                graph.read(filePath);
            }
            catch (org.graphstream.stream.GraphParseException gpe)
            {
                gpe.printStackTrace();
            }
            System.out.println("Graph loaded from file:");
            System.out.println("Number of nodes: " + graph.getNodeCount());
            System.out.println("Number of edges: " + graph.getEdgeCount());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return graph;
    }

    private double calculateNetworkEfficiency(Graph graph) {
        double sumInversePathLengths = 0.0;
        int numNodes = graph.getNodeCount();

        Iterator<Node> nodeIterator = graph.iterator();
        while (nodeIterator.hasNext()) {
            Node source = nodeIterator.next();
            Dijkstra dijkstra = new Dijkstra(Dijkstra.Element.EDGE, null, "length");
            dijkstra.init(graph);
            dijkstra.setSource(source);
            dijkstra.compute();

            nodeIterator = graph.iterator(); // Reset the iterator to start from the beginning

            while (nodeIterator.hasNext()) {
                Node target = nodeIterator.next();
                if (!target.equals(source)) {
                    double pathLength = dijkstra.getPathLength(target);
                    if (pathLength != Double.POSITIVE_INFINITY) {
                        sumInversePathLengths += 1.0 / pathLength;
                    }
                }
            }
        }

        return sumInversePathLengths / (numNodes * (numNodes - 1));
    }

    private double calculateWeightedAveragePathLength(Graph graph) {
        double sumPathLengths = 0.0;
        int numPaths = 0;

        Iterator<Node> nodeIterator = graph.iterator();
        while (nodeIterator.hasNext()) {
            Node source = nodeIterator.next();
            Dijkstra dijkstra = new Dijkstra(Dijkstra.Element.EDGE, null, "length");
            dijkstra.init(graph);
            dijkstra.setSource(source);
            dijkstra.compute();

            nodeIterator = graph.iterator(); // Reset the iterator to start from the beginning

            while (nodeIterator.hasNext()) {
                Node target = nodeIterator.next();
                if (!target.equals(source)) {
                    double pathLength = dijkstra.getPathLength(target);
                    if (pathLength != Double.POSITIVE_INFINITY) {
                        sumPathLengths += pathLength;
                        numPaths++;
                    }
                }
            }
        }

        return sumPathLengths / numPaths;
    }

    private void displayGraph(Graph graph) {
        // Setup the graph view with proper attributes
        graph.setAttribute("ui.quality");
        graph.setAttribute("ui.antialias");

        // Create the Viewer and add the graph to it
        //viewer = new Viewer(graph, Viewer.ThreadingModel.GRAPH_IN_GUI_THREAD);
        viewer.enableAutoLayout();

        // Get the DefaultView from the viewer
        //DefaultView view = (DefaultView) viewer.addDefaultView(false);

        // Add the DefaultView to the graphViewPanel
        //graphViewPanel.removeAll();
        //graphViewPanel.add((Component) viewer);
        //graphViewPanel.revalidate();

        // Start the viewer
        //viewer.enableAutoLayout();
        //viewer.setCloseFramePolicy(Viewer.CloseFramePolicy.EXIT);
        //viewer.addView(viewer);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GraphAnalyzerGUI::new);
    }
}
