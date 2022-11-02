import React from "react";
import Chart from "react-apexcharts";

class Pie extends React.Component {
    constructor(props) {
        super(props);

        this.state = {

        series: [44, 55, 13, 43, 45],
            options: {
                chart: {
                    height: 350,
                    type: 'pie',
                    toolbar: {
                        show: true
                    }
                },
                title: {
                    text: 'Perentage deferred in year wise',
                    align: 'left'
                },
                legend: {
                    position: 'bottom',
                    horizontalAlign: 'right',
                    floating: true,
                    offsetY: 12,
                    offsetX: 0
                },
                labels: ['2018', '2019', '2020', '2021', '2022'],
                responsive: [{
                    breakpoint: 480,
                    options: {
                        chart: {
                            width: 200
                        },
                        legend: {
                            position: 'bottom',
                        }
                    }
                }]
            },
        };
    }


    render() {
        return (
            <div>
                <div id="chart">
                    <Chart options={this.state.options} series={this.state.series} type="pie" width={380} />
                </div>
            </div>
        );
    }
}

export default Pie;