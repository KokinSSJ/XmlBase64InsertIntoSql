package com.mlmb.XBSConverter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;

/**
 * XmlBase64SqlConverter -> takes xml converts to base64 and change with sql
 * insert query in correspond file.
 * 
 * @author Kokin
 *
 */
public class XBSConverter {
	
	
	private static String pathToXml = "./XMLfolder";
	private static String pathToSql = "./SQLfolder";
	
	private static List<FileConverter> filesConverters;
	private static List<FileConverter> toDeleteConverters = new ArrayList<>();
	
	public static void main(String[] args) throws IOException {
		// init from sql folder
		filesConverters = Files.list(Paths.get(pathToSql))
				.filter(t -> t.toString().toLowerCase().endsWith(".sql"))
				.map(FileConverter::new)
				.collect(Collectors.toList());
		// check if xml files exists for sql files
		filesConverters.stream().forEach(XBSConverter::checkIfXmlFileExists);
		filesConverters.removeAll(toDeleteConverters);
		
		//generate base64 of xml file and add to fileConverter
		filesConverters.stream().forEach(XBSConverter::setBase64);
		
		//set sql body
		filesConverters.stream().forEach(XBSConverter::setSqlBody);
		
		// change string sql that has "decode=('" and end with "'" and save as newBody in file
		filesConverters.stream().forEach(XBSConverter::changeBinaryInSqlWhichConsistDecode);
		
		//forEach update Sql files
		filesConverters.stream().forEach(XBSConverter::updateSqlFile);
		
		System.out.println(filesConverters);

		//

		// // all xml files
		// Stream<Path> inputXML = Files.list(Paths.get(pathToSql))
		// .filter(t -> t.toString().toLowerCase().endsWith(".sql"));
		//// inputXML.filter(XBSConverter::checkIfXmlFileExists);
		//
		// // map key = path to sql file / value = base64 of xml
		// Map<Path, String> map = inputXML.collect(Collectors.toMap(i-> i, i ->
		// Base64.getEncoder().encodeToString(encodeXmlToBase64String(i))));
		// System.out.println(map);
		// // ENTRYSET!!
		// // existed sql files from xml files list
		// Stream<Path> sqlFiles2 = map.keySet().stream().map(path ->
		// getFileNameWithoutExtension(path))
		// .map(XBSConverter::buildPathString)
		// .map(Paths::get).map(path -> checkIfFileExist(path));
		//
		// // sql bodies filtered by "decode=('"
		// Stream<List<String>> filesFiltered =
		// sqlFiles2.map(XBSConverter::readAllLinesToList)
		// // .map(el -> el.stream().reduce((x, y) -> x + "\n" +
		// // y).orElse(""))
		// .filter(XBSConverter::matchLine);
		// // x, y -> x + "\n" + y
		// // filesFiltered.forEach(EncodeXmlToSqlFiles::matchLine2);
		//
		//
		// // System.out.println(.collect(Collectors.toList()));
		//// .map(Base64.getEncoder()::encodeToString);
		
	}

	private static void checkIfXmlFileExists(FileConverter file) {
		Path xmlPathFromSqlPath = getXmlPathFromSqlPath(file.getPathToSql());
		if (xmlPathFromSqlPath.toFile().exists()) {
			file.setPathToXml(xmlPathFromSqlPath);
		} else {
			toDeleteConverters.add(file); // to bedzie krzaczyæ app?! bo sie
											// usuwa a potem przesunie sie
											// element?
			System.out.println("Sorry mate, but I cannot find XML file: " + xmlPathFromSqlPath);
		}
	}

	private static Path getXmlPathFromSqlPath(Path path) {
		String name = path.toFile().getName();
		return Paths.get(buildPathStringToXmlFile(name.substring(0, name.indexOf(".sql"))));
	}

	private static void setBase64(FileConverter file) {
		file.setBase64(Base64.getEncoder().encodeToString(encodeXmlToBase64String(file.getPathToXml())));
	}

	private static void setSqlBody(FileConverter file) {
		file.setSqlBody(readAllLinesToList(file.getPathToSql()));
	}

	private static void changeBinaryInSqlWhichConsistDecode(FileConverter file) {
		// dodaj wielkosc pliku
		Optional<String> reducedSqlBody = file.getSqlBody().stream().reduce((x, y) -> x + "\n" + y);
		file.setNewSqlBody(
				reducedSqlBody.map(docString -> changeBase64InQuery(docString, file)).orElse(null));
	}

	private static String changeBase64InQuery(String document, FileConverter fileConverter) {
		String match = "decode=('";
		int baseStart = document.indexOf(match);
		int baseStop = document.indexOf("')", baseStart);
		if (baseStart == -1 || baseStop == -1) {
			System.out.println("SQL file malformed " + fileConverter.getPathToSql());
			return document;
		}
		System.out.println("SQL file ok: " + fileConverter.getPathToSql());
		return document.substring(0, baseStart + match.length()) + fileConverter.getBase64()
				+ document.substring(baseStop);
	}

	private static void updateSqlFile(FileConverter file) {
		try {
			FileUtils.writeStringToFile(file.getPathToSql().toFile(), file.getNewSqlBody(), "UTF-8", false);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// private static void matchLine2(String line, Map.Entry<Path, String> row)
	// {
	// int baseStart = line.indexOf("decode=('");
	// int baseStop = line.indexOf("'", baseStart);
	// String newFile = line.substring(0, baseStart) + row.getValue() +
	// line.substring(baseStop);
	// // try {
	// //// FileUtils.writeStringToFile(row.getKey().toFile(), newFile,
	// // "UTF-8", false);
	// // } catch (IOException e) {
	// // // TODO Auto-generated catch block
	// // e.printStackTrace();
	// // }
	// }
	//
	// private static boolean matchLine(List<String> lines) {
	// return lines.stream().anyMatch(line -> line.contains("decode=('"));
	// }


	private static List<String> readAllLinesToList(Path t) {
		try {
			return Files.readAllLines(t);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Collections.emptyList();
	}

	// private static Path checkIfFileExist(Path path) {
	// if(path.toFile().exists()) {
	// return path;
	// }
	// System.out.println("File: " + path.toFile().getName() + " doesn't
	// exists");
	// return null;
	// }
	// public static String buildPathString(String fileName) {
	// return pathToSql + "/" + fileName + ".sql";
	// }

	public static String buildPathStringToXmlFile(String fileName) {
		return pathToXml + "/" + fileName + ".xml";
	}

	
	// @FunctionalInterface
	// interface SupplierEncode<T> {
	// T get(Path path) throws IOException;
	// }
	//
	// private static <T> T getFileBody(SupplierEncode<T> supplier, Path path) {
	// try {
	// return supplier.get(path);
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// return null;
	// }

	private static byte[] encodeXmlToBase64String(Path path) {
		try {
			return Files.readAllBytes(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
