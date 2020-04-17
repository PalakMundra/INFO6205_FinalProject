package edu.northeastern.eplranking.ui.controller;

/*
public class TeamVsXPDController {
    @FXML
    private ComboBox<Team> comboTeam;

    @FXML
    private LineChart<Integer, Double> pdChart;

    @FXML
    private void initialize() {
        List<Team> teamList = GameData.getInstance().getTeamList();
        comboTeam.setItems(FXCollections.observableArrayList(teamList));

        pdChart.setTitle("Probability Distribution");
    }

    @FXML
    private void generate() {
        Team selectedTeam = comboTeam.getSelectionModel().getSelectedItem();

        List<PDModel> pdModelList = GameData.getInstance().getPdModelList();

        List<PDModel> list = pdModelList.stream().filter(pdModel -> pdModel.getTeam1().equals(selectedTeam)).collect(Collectors.toList());
        list.addAll(pdModelList.stream().filter(pdModel -> pdModel.getTeam2().equals(selectedTeam)).map(oldPdModel -> {
            PDModel inverted = new PDModel();
            inverted.setTeam1(oldPdModel.getTeam2());
            inverted.setTeam2(oldPdModel.getTeam1());
            inverted.setProbabilityDistribution(oldPdModel.getProbabilityDistribution().entrySet().stream().collect(Collectors.toMap(integerDoubleEntry -> integerDoubleEntry.getKey() * -1, e -> e.getValue())));
            return inverted;
        }).collect(Collectors.toList()));


        ObservableList<XYChart.Series<Integer,Double>> seriesList = FXCollections.observableArrayList();
        for (PDModel model : list) {
            XYChart.Series<Integer, Double> series = new XYChart.Series<>();
            series.setData(FXCollections.observableArrayList(model.getProbabilityDistribution().entrySet().stream().map(e -> new XYChart.Data<>(e.getKey(), e.getValue())).collect(Collectors.toList())));
            series.setName("vs " + model.getTeam2().getName());
            seriesList.add(series);
        }
        pdChart.setData(seriesList);
    }
}*/
