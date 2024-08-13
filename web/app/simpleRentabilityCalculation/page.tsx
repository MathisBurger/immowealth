"use client";

import {Button, ButtonGroup, Card, CardContent, Divider, Slider, Typography, TypographyProps} from "@mui/joy";
import {useTranslation} from "next-export-i18n";
import {useMemo, useState} from "react";

const scaleOptions = [
    {value: 1000, label: '1k'},
    {value: 5000, label: '5k'},
    {value: 10000, label: '10k'},
    {value: 100000, label: '100k'}
]

const SimpleRentabilityCalculation = () => {

    const {t} = useTranslation();

    const [scale, setScale] = useState<number>(scaleOptions[0].value);
    const rentScale = useMemo<number>(() => scale * 0.06 / 12, [scale]);
    const [rentPerMonth, setRentPerMonth] = useState<number>(0);
    const [price, setPrice] = useState<number>(0);

    const rentability = useMemo<number>(() => rentPerMonth*12/price*100, [rentPerMonth, price]);

    const rentabilityColor = useMemo<TypographyProps["color"]>(
        () => {
            if (rentability < 2) {
                return "danger";
            }
            if (rentability > 5.5) {
                return "success";
            }
            return "warning";
        },
        [rentability]
    )
    return (
        <>
            <Typography level="h1">
                {t('routes.simpleRentabilityCalculation')}
            </Typography>
            <Divider />
            <Card>
                <CardContent>
                    <Typography level="h3" color={rentabilityColor}>{rentability.toFixed(2)}%</Typography>
                </CardContent>
            </Card>
            <ButtonGroup>
                {scaleOptions.map((option) => (
                    <Button
                        key={option.label}
                        color={option.value === scale ? "primary" : "neutral"}
                        onClick={() => setScale(option.value)}
                    >{option.label}</Button>
                ))}
            </ButtonGroup>
            <Slider
                aria-label="Always visible"
                value={price}
                onChange={(_, p) => setPrice(p as number)}
                step={scale}
                min={0}
                max={scale*100}
                valueLabelDisplay="on"
            />
            <Slider
                aria-label="Always visible"
                value={rentPerMonth}
                onChange={(_, p) => setRentPerMonth(p as number)}
                step={rentScale}
                min={0}
                max={rentScale*100}
                valueLabelDisplay="on"
            />
        </>
    );
}

export default SimpleRentabilityCalculation;