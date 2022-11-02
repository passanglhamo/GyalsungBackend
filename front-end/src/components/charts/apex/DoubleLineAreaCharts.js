
import React from "react";
import Chart from "react-apexcharts";

class DoubleLineAreaCharts extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            series: [
                {
                    name: "Male",
                    data: [2800, 3123, 2700, 3984, 2943]
                },
                {
                    name: "Female",
                    data: [3000, 1987, 2604, 2874, 3020]
                }
            ],
            options: {
                chart: {
                    height: 350,
                    type: 'area'
                },
                dataLabels: {
                    enabled: false
                },
                colors: ['#615A59', '#fca2a2'],
                dataLabels: {
                    enabled: false,
                },
                stroke: {
                    curve: 'smooth',
                    width: 2,
                },
                title: {
                    text: 'Gender wise total registered area chart',
                    align: 'left'
                },

                xaxis: {
                    categories: ['2018', '2019', '2020', '2021', '2022'],
                    title: {
                        text: 'Year'
                    }
                },
                yaxis: {
                    title: {
                        text: 'Figure'
                    },
                },
                legend: {
                    position: 'bottom',
                    horizontalAlign: 'right',
                    floating: true,
                    offsetY: 5,
                    offsetX: 5
                }
            },
        };
    }

    render() {
        return (
            <div>
                <div id="chart">
                    <Chart options={this.state.options} series={this.state.series} type="area" height={350} />
                </div> 
            </div>
        );
    }
}

export default DoubleLineAreaCharts;