package husaccttest.validate.benchmark.testfiles;

import husacct.validate.domain.validation.DefaultSeverities;
import husacct.validate.domain.validation.Severity;
import husacct.validate.domain.validation.Violation;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ViolationsRuleXML {
	private static final String FILE_PRE_NAME = "src/husaccttest/validate/benchmark/testfiles/violations_rule_";
	private static final String FILE_EXTENSION = ".xml";

	public ViolationsRuleXML() {

	}

	public ArrayList<Violation> giveViolationsOutXMLFile(int fileNumber) {
		ArrayList<Violation> foundedViolations = new ArrayList<Violation>();
		if(checkIfXMLFileExists(fileNumber)) {
			String filePath = getFileNameFromFileNumber(fileNumber);
			foundedViolations = readXMLFile(filePath);
		}

		return foundedViolations;
	}

	private ArrayList<Violation> readXMLFile(String filePath) {
		ArrayList<Violation> xmlViolations = new ArrayList<Violation>();


		// Create a factory
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		// Use the factory to create a builder
		DocumentBuilder builder;
		try {
			builder = factory.newDocumentBuilder(); System.err.println("PATH: " + filePath);
			Document xmlDocument = builder.parse(filePath);

			// Get a list of all elements in the document
			NodeList elementList = xmlDocument.getElementsByTagName("violation");
			System.out.println("=> XML Elements: " + ((Element)elementList.item(0)).getNodeName()
					+ "\nLengte: " + elementList.getLength());
			
			for (int counter = 0; counter < elementList.getLength(); counter++) { System.out.println(counter);
				// Get element
				Violation xmlViolation = new Violation();
				Element element = (Element)elementList.item(counter);
				
				System.out.println(">> Name: " + element.getNodeName() + " - value: " + element.getNodeValue()
						+ "\n Lengte child: " + element.getChildNodes().getLength());
				//getElementsByTagName("firstname").item(0).getTextContent()
				
				xmlViolation.setClassPathFrom(element.getAttribute("source"));
				xmlViolation.setClassPathTo(element.getAttribute("target"));
				xmlViolation.setLineNumber(Integer.parseInt(element.getAttribute("lineNr")));
				Severity violationSeverity = new Severity(
						DefaultSeverities.valueOf(element.getAttribute("severity").toUpperCase()).toString(), 
						DefaultSeverities.valueOf(element.getAttribute("severity").toUpperCase()).getColor());
				xmlViolation.setSeverity(violationSeverity);
				xmlViolation.setRuletypeKey(element.getAttribute("ruleType"));
				xmlViolation.setViolationTypeKey(element.getAttribute("dependencyKind"));
				String booleanString = element.getAttribute("isDirect");
				booleanString = booleanString.substring(0, 1).toUpperCase() + booleanString.substring(1).toLowerCase();
				xmlViolation.setInDirect(Boolean.valueOf(booleanString));
				xmlViolations.add(xmlViolation);
			}
		}
		catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}

		return xmlViolations;
	}


	private boolean checkIfXMLFileExists(int fileNumber) {
		boolean returnCondition = false;

		try {
			File file = new File(getFileNameFromFileNumber(fileNumber));

			if(file.exists()) {
				returnCondition = true;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return returnCondition;
	}

	private String getFileNameFromFileNumber(int fileNumber) {
		if(fileNumber < 10) {
			return FILE_PRE_NAME + "0" + fileNumber + FILE_EXTENSION;
		}
		else {
			return FILE_PRE_NAME + fileNumber + FILE_EXTENSION;
		}
	}

}
