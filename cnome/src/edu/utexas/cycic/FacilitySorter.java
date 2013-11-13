package edu.utexas.cycic;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import edu.utah.sci.cyclist.ui.components.ViewBase;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.control.*;

public class FacilitySorter extends ViewBase {

	public FacilitySorter(){
		super();
		setWidth(780);
		setMaxWidth(900);
		setMinWidth(780);
		setHeight(300);
		setMaxHeight(300);
		init ();
	}

	static BorderPane pane= new BorderPane();
	static Pane display=new Pane(){
		{
			setMinSize(250,150);
		}
	};
	static ArrayList<Object> structure=new ArrayList<>();
	static Integer facilityIndex;
	static ArrayList<Object> displayList=new ArrayList<>();

	private void init (){
		//initialize most variables
		final Text list=new Text ();
		HBox topBox= new HBox();
		topBox.setSpacing(50);


		final HBox searchBox=new HBox(){
			{
				setSpacing(10);
				setMaxWidth(600);
				setMaxHeight(35);
				setStyle("-fx-border-color: black;");
			}
		};

		final ComboBox<String> facilityList=new ComboBox<String>(){
			{
				setVisibleRowCount(6);
			}
		};
		searchBox.getChildren().add(facilityList);

		final Label number=new Label();
		searchBox.getChildren().add(number);


		
		final Slider numberShowen=new Slider(0,0,0){
			{
				setShowTickMarks(true);
				setShowTickLabels(true);
				setMinorTickCount(0);
				setValue(getMax());
				setMajorTickUnit(1);
				setSnapToTicks(true);
				setMax(displayList.size());
			}
		};

		numberShowen.valueProperty().addListener(new ChangeListener<Number>(){
			public void changed(ObservableValue<? extends Number> ov,Number old_val, Number new_val) {
				number.setText("number of nodes shown:" +Integer.toString(new_val.intValue()));
				display.getChildren().clear();
				for (int iii=0;iii<new_val.intValue();iii++){
					Object appear=displayList.get(iii);
					display.getChildren().add(((facilityNode)appear).sorterCircle);
					display.getChildren().add(((facilityNode)appear).sorterCircle.text);
				}
			}
		});
		
		searchBox.getChildren().add(numberShowen);

		final Label type=new Label();
		searchBox.getChildren().add(type);

		final VBox criteria = new VBox(){
			{
				setSpacing(10);
				setStyle("-fx-border-color: black;");
				setPrefHeight(getHeight());
			}
		};
		final Button add= new Button();
		add.setText("add Criteria");

		final Button clear=new Button(){
			{
				setText("Clear All");
			}
		};

		final VBox addMore= new VBox();
		addMore.setSpacing(10);
		final ArrayList<Object>data=new ArrayList<>();
		final ArrayList<Object> filter=new ArrayList<>();
		//variables default info


		final Button start=new Button(){
			{
				setText("Start");
			}
		};


		topBox.getChildren().add(searchBox);
		topBox.getChildren().add(criteria);


		//right criteria column 
		//drop down for all parent nodes
		for(int i =0; i < CycicScenarios.workingCycicScenario.FacilityNodes.size(); i++){
			facilityList.getItems().add((String) CycicScenarios.workingCycicScenario.FacilityNodes.get(i).name);
		}

		//read chosen facility parent node, update slider bar, type, and searchlist 
		facilityList.valueProperty().addListener(new ChangeListener<String>(){
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				//find the location of chosen node in the data list
				criteria.getChildren().clear();
				structure.clear();
				display.getChildren().clear();
				data.clear();
				for(int i = 0; i < CycicScenarios.workingCycicScenario.FacilityNodes.size(); i++){
					if(newValue == CycicScenarios.workingCycicScenario.FacilityNodes.get(i).name){
						facilityIndex = i;
						break;
					}
				}
				//numberShowen.setMax(CycicScenarios.workingCycicScenario.FacilityNodes.get(facilityIndex).facilityClones.size());
				//update data array
				for (int i=0;i<CycicScenarios.workingCycicScenario.FacilityNodes.get(facilityIndex).facilityStructure.size();i++){
					ArrayList<Object>element=new ArrayList<>();
					for (int ii=0;ii<CycicScenarios.workingCycicScenario.FacilityNodes.get(facilityIndex).facilityClones.size();ii++){
						ArrayList <Object>Subelement=new ArrayList<>();
						ArrayList <Object> check= (ArrayList )CycicScenarios.workingCycicScenario.FacilityNodes.get(facilityIndex).facilityClones.get(ii).facilityData.get(i);
						if (check instanceof ArrayList){
							for (int iii=0;iii<check.size();iii++){
								ArrayList <Object>moreThanOneCase=new ArrayList<>();
								moreThanOneCase.add(check.get(iii));
								moreThanOneCase.add(CycicScenarios.workingCycicScenario.FacilityNodes.get(facilityIndex).facilityClones.get(ii));
								Subelement.add(moreThanOneCase);

							}

						}
						else {
							Subelement.add(check);
							Subelement.add(CycicScenarios.workingCycicScenario.FacilityNodes.get(facilityIndex).facilityClones.get(ii));
						}
						element.add(Subelement);
					}
					data.add(element);
				}
				/*	for (int i=0;i<numberShowen.getMax();i++){
display.getChildren().add(CycicScenarios.workingCycicScenario.FacilityNodes.get(facilityIndex).facilityClones.get(i).sorterCircle);
display.getChildren().add(CycicScenarios.workingCycicScenario.FacilityNodes.get(facilityIndex).facilityClones.get(i).sorterCircle.text);
}
				 */
				number.setVisible(true);
				number.setText("number of nodes shown:" +Integer.toString(CycicScenarios.workingCycicScenario.FacilityNodes.get(facilityIndex).facilityClones.size()));

				//update information in searchbox
				type.setText("Type: "+CycicScenarios.workingCycicScenario.FacilityNodes.get(facilityIndex).facilityType);

				formSort(CycicScenarios.workingCycicScenario.FacilityNodes.get(facilityIndex).facilityStructure,data,structure);
				//System.out.println(data);
				//System.out.println(CycicScenarios.workingCycicScenario.FacilityNodes.get(i).facilityStructure);
				//System.out.println(structure);
				criteria.getChildren().add(add);
				criteria.getChildren().add(clear);
				criteria.getChildren().add(addMore);
				criteria.getChildren().add(start);
				initialize( displayList);


			}
		});

		//clear function
		clear.setOnMouseClicked(new EventHandler<MouseEvent>(){

			public void handle (MouseEvent e){
				addMore.getChildren().clear();
				initialize (displayList);
			}
		});


		//add button function. add addmore and filter button
		add.setOnMouseClicked(new EventHandler <MouseEvent>(){
			@Override
			public void handle(MouseEvent e) {
				final HBox detailBox=new HBox();
				detailBox.setSpacing(5);
				ComboBox<String> detail=new ComboBox<String>();
				//data
				final ArrayList<ArrayList> listed=new ArrayList<>();
				//stucture for the term
				final ArrayList<ArrayList> struc=new ArrayList<>();
				//
				final ArrayList<Object> order=new ArrayList<>();
				for (int k=0;k<structure.size();k++){
					ArrayList<ArrayList>detailInfo=(ArrayList<ArrayList>)structure.get(k);
					detail.getItems().add((String) detailInfo.get(0).get(0));
					order.add((String) detailInfo.get(0).get(0));
					listed.add(detailInfo.get(1));
					struc.add(detailInfo.get(0));
				}
				detail.setVisibleRowCount(7);
				detailBox.getChildren().add(detail);
				addMore.getChildren().add(detailBox);

				//	System.out.println(CycicScenarios.workingCycicScenario.FacilityNodes.get(0).facilityData);
				//	System.out.println(data);
				detail.valueProperty().addListener(new ChangeListener <String>(){
					@Override
					public void changed(ObservableValue<? extends String> arg0,	String oldValue, final String newValue) {
						for ( int i = 0; i < order.size(); i++){
							if(order.get(i) == newValue){
								final int listOrder=i;
								if (struc.get(i).get(2).equals("String")){
									if (detailBox.getChildren().size()>=1){
										detailBox.getChildren().remove(1);
									}
									final ComboBox <String> information=new ComboBox<>();
									for (int ii=0;ii<((ArrayList<Object>) listed.get(i)).size();ii++){
										ArrayList<ArrayList>newList=(ArrayList<ArrayList>)listed.get(i);
										if(!(information.getItems().contains(((ArrayList<Object>) newList.get(ii).get(0)).get(0)))){
											information.getItems().add(((ArrayList) newList.get(ii).get(0)).get(0).toString());
										}

									}
									detailBox.getChildren().add(information);
									information.valueProperty().addListener(new ChangeListener <String>(){

										@Override
										public void changed(ObservableValue<? extends String> arg,
												String oldProperty, String newProperty) {
											ArrayList <Object>subfilt=new ArrayList<>();
											subfilt.add(listOrder);
											subfilt.add(newProperty);
											filter.add(subfilt);	
										}
									});
								}
								else {
									VBox range=new VBox();
									HBox lB=new HBox();
									HBox UB =new HBox();
									Text lbNum=new Text();
									Text uBNum=new Text();
									Button confirm=new Button();
									confirm.setText("confirm");
									lbNum.setText("LB");
									uBNum.setText("UB");
									lB.getChildren().add(lbNum);
									final TextField lBValue=new TextField();
									lBValue.setPromptText("Number Only");
									final TextField uBValue=new TextField();
									uBValue.setPromptText("Number Only");

									lB.getChildren().add(lBValue);
									UB.getChildren().add(uBNum);
									UB.getChildren().add(uBValue);
									range.getChildren().add(lB);
									range.getChildren().add(UB);
									range.getChildren().add(confirm);
									confirm.setOnAction(new EventHandler<ActionEvent>() {
										@Override
										public void handle(ActionEvent e) {
											if (!(isInteger(lBValue.getText()))){
												lBValue.setText("PLEASE!!!NUMBERS!!!");
											} else if (!(isInteger(uBValue.getText()))){
												uBValue.setText("PLEASE!!!NUMBERS!!!");
											} else if (Integer.parseInt(lBValue.getText())>Integer.parseInt(uBValue.getText())){
												lBValue.setText("PLEASE ENTER CORRECTLY");
												uBValue.setText("PLEASE ENTER CORRECTLY");
											}else {
												int lowvalue;
												int upvalue;
												lowvalue=Integer.parseInt(lBValue.getText());
												upvalue=Integer.parseInt(uBValue.getText());
												ArrayList <Object>Subfilt=new ArrayList<>();
												Subfilt.add(listOrder);
												Subfilt.add(lowvalue);
												Subfilt.add(upvalue);
												filter.add(Subfilt);
											}
										}
									});
									if (detailBox.getChildren().size()>=1){
										detailBox.getChildren().remove(1);
									}
									detailBox.getChildren().add(range);

								}
							}
						}
					}

				});
				start.setOnMouseClicked(new EventHandler <MouseEvent>(){

					@Override
					public void handle(MouseEvent event) {
						for (int i = 0;i < filter.size();i++){
							ArrayList<Object> fit=new ArrayList<>();
							if (((ArrayList<Object>) filter.get(i)).size() < 3){
								ArrayList <Object> choose= listed.get((int) ((ArrayList<Object>) filter.get(i)).get(0));
								for (int ii = 0; ii < choose.size(); ii++){
									if (((ArrayList<Object>) choose.get(ii)).get(1).equals(((ArrayList<Object>) filter.get(i)).get(1))){
										fit.add(((ArrayList<Object>) filter.get(i)).get(0));
									}
								}
							} else {
								ArrayList <Object> choose= listed.get((int) ((ArrayList<Object>) filter.get(i)).get(0));
								for (int ii = 0; ii < choose.size(); ii++){
									int testNumber = Integer.parseInt(((ArrayList<Object>) choose.get(ii)).get(1).toString());
									int upperBound = Integer.parseInt(((ArrayList<Object>) filter.get(i)).get(2).toString());
									int lowerBound = Integer.parseInt(((ArrayList<Object>) filter.get(i)).get(1).toString());
									if (testNumber <=upperBound && testNumber >= lowerBound){
										fit.add(((ArrayList<Object>) filter.get(i)).get(0));
									}
								}
							}
							ArrayList<Object> match=new ArrayList<>();
							//update the displaylist
							for (int ii = 0;ii < displayList.size();ii++){
								if (fit.contains(displayList.get(ii))){
									match.add(displayList.get(ii));
								}
							}
							displayList.clear();
							for (int ii=0;ii <match.size(); ii++){
								displayList.add(match.get(ii));
							}
						}
					}
				});

				detailBox.getChildren().add(list);
			}
		});


		pane.setTop(topBox);
		pane.setLeft(display);

		setContent (pane);

	}
	public static boolean isInteger(String s) {
		try { 
			Integer.parseInt(s); 
		} catch(NumberFormatException e) { 
			return false; 
		}
		// only got here if we didn't return false
		return true;
	}

	protected void initialize(ArrayList<Object>al){
		al.clear();
		for (int i = 0;i < CycicScenarios.workingCycicScenario.FacilityNodes.get(facilityIndex).facilityClones.size();i++){
			al.add(CycicScenarios.workingCycicScenario.FacilityNodes.get(facilityIndex).facilityClones.get(i));
		}
	}

	//need fix the data for this
	protected void formSort(ArrayList<Object> struc, ArrayList<Object> data,ArrayList<Object> facilitySortArray){
		for (int i=0;i<struc.size();i++){
			if (struc.size()>2){
				if (struc.get(i) instanceof ArrayList && struc.get(0) instanceof ArrayList){
					formSort((ArrayList<Object>)struc.get(i),(ArrayList<Object>)data.get(i),facilitySortArray);
				}else if (i==0){
					if (struc.get(2)=="oneOrMore"||struc.get(2)=="zeroOrMore"){
						ArrayList <Object> newData=new ArrayList<>();
						ArrayList<Object> newStruc=(ArrayList<Object>)struc.get(1);
						for (int ii=0;ii<newStruc.size();ii++){
							ArrayList<Object> element=new ArrayList<>();
							for (int iii=0;iii<data.size();iii++){
								ArrayList<Object> subData=(ArrayList<Object>) data.get(iii);
								ArrayList<Object>dataCol=(ArrayList<Object>)subData.get(0);
								element.add(dataCol.get(ii));
							}
							newData.add(element);
						}
						formSort(newStruc,newData,facilitySortArray);
					}else if (struc.get(1) instanceof ArrayList){
						ArrayList <Object> newData=new ArrayList<>();
						ArrayList<Object> newStruc=(ArrayList<Object>)struc.get(1);
						for (int ii=0;ii<newStruc.size();ii++){
							System.out.println(newStruc.get(i));
							ArrayList<Object> element=new ArrayList<>();
							for (int iii=0;iii<data.size();iii++){
								ArrayList<Object> subData=(ArrayList<Object>) data.get(iii);
								ArrayList<Object> dataCol=(ArrayList<Object>)subData.get(0);
								System.out.println(dataCol);
								element.add(dataCol.get(ii));
							}
							newData.add(element);
						}
						formSort(newStruc,newData,facilitySortArray);

					}else {
						ArrayList<Object> element=new ArrayList<Object>();
						element.add(struc);
						element.add(data);
						if (!(facilitySortArray.contains(element))){
							facilitySortArray.add(element);
						}	
					}
				}
			} else formSort((ArrayList<Object>)struc.get(i),(ArrayList<Object>)data.get(i),facilitySortArray);
		}
	}	
}