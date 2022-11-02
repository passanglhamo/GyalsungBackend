
import React from "react";
import Chart from "react-apexcharts";

class DoubleLineCharts extends React.Component {
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
                    type: 'area',
                    dropShadow: {
                        enabled: true,
                        color: '#000',
                        top: 18,
                        left: 7,
                        blur: 10,
                        opacity: 0.2
                    },
                    toolbar: {
                        show: true
                    },
                    animations: {
                        enabled: true,
                        easing: 'linear',
                        speed: 800,
                        animateGradually: {
                            enabled: true,
                            delay: 150
                        },
                        dynamicAnimation: {
                            enabled: true,
                            speed: 350
                        }
                    }
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
                    text: 'Gender wise total registered line chart',
                    align: 'left'
                },
                grid: {
                    borderColor: '#e7e7e7',
                    row: {
                        colors: ['#f3f3f3', 'transparent'], // takes an array which will be repeated on columns
                        opacity: 0.3
                    },
                },
                markers: {
                    size: 1
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
                    // min: 5,
                    // max: 40
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
                    <Chart options={this.state.options} series={this.state.series} type="line" height={350} />
                </div>
            </div>
        );
    }
}

export default DoubleLineCharts;