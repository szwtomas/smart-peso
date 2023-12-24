from datetime import datetime
from datetime import timedelta


def getBlueAndOfficialPrices():
    blue = []
    official = []
    with open("./evolution.csv", "r") as f:
        lines = [line.rstrip() for line in f]
        for l in lines[1:]:
            splitted = l.split(",")
            date = splitted[0]
            year = date.split("-")[0]
            if year != '2023' and year != '2022':
                continue

            if splitted[1] == "Blue":
                blue.append({"date": datetime.strptime(
                    date, '%Y-%m-%d'), "price": float(splitted[3])})
            elif splitted[1] == "Oficial":
                official.append({"date": datetime.strptime(
                    date, '%Y-%m-%d'), "price": float(splitted[3])})
            else:
                continue

    return official, blue


def get_ccl_prices():
    ccl = []
    with open("./ccl.csv", "r") as f:
        lines = [line.rstrip() for line in f]
        for l in lines:
            split = l.split("\t")
            ccl.append({"date": datetime.strptime(
                split[0], '%d/%m/%Y'), "price": float(split[1].replace(",", "."))})
    return ccl


def get_mep_prices():
    mep = []
    with open("./mep.csv", "r") as f:
        lines = [line.rstrip() for line in f]
        for l in lines:
            split = l.split("\t")
            mep.append({"date": datetime.strptime(
                split[0], '%d/%m/%Y'), "price": float(split[1].replace(",", "."))})
    return mep


def prices_by_date(input_dict):
    output = []
    dates = set()

    # Gather all unique dates across all lists
    for key, values in input_dict.items():
        for entry in values:
            if entry['date'] not in dates:
                dates.add(entry['date'])

    # Process data for each unique date
    for date in dates:
        price_dict = {}
        discard_date = False

        for key, values in input_dict.items():
            price_found = False
            for entry in values:
                if entry['date'] == date:
                    price_dict[key] = entry['price']
                    price_found = True
                    break

            # If the price for a key is missing on a date, discard that date
            if not price_found:
                discard_date = True
                break

        if not discard_date:
            output.append({'date': date, 'prices': price_dict})

    return output


def fill_missing_dates(data_list):
    min_date = min(entry['date'] for entry in data_list)
    max_date = max(entry['date'] for entry in data_list)

    existing_dates = {entry['date'] for entry in data_list}

    current_date = min_date
    filled_list = []
    while current_date <= max_date:
        if current_date in existing_dates:
            for entry in data_list:
                if entry['date'] == current_date:
                    filled_list.append(entry)
                    break
        else:
            filled_list.append({'date': current_date, 'prices': {}})
        current_date += timedelta(days=1)

    return filled_list


def fill_missing_prices(prices):
    for i in range(len(prices)):
        p = prices[i]
        if len(p["prices"]) != 4:
            for j in range(i - 1, 0, -1):
                if len(prices[j]["prices"]) == 4:
                    new_prices = prices[j]["prices"].copy()
                    prices[i]["prices"] = new_prices
                    break
    return prices


official, blue = getBlueAndOfficialPrices()
ccl = get_ccl_prices()
mep = get_mep_prices()

prices = {
    "usdBlue": blue,
    "usdOfficial": official,
    "usdCCL": ccl,
    "usdMEP": mep,
}

prices = prices_by_date(prices)
prices = fill_missing_dates(prices)
prices = fill_missing_prices(prices)

with open("prices.csv", "w") as f:
    f.write("date,official,mep,ccl,blue\n")
    for p in prices:
        price = p["prices"]
        s = p["date"].strftime("%d/%m/%Y") + ","
        s += str(price["usdOfficial"]) + ","
        s += str(price["usdMEP"]) + ","
        s += str(price["usdCCL"]) + ","
        s += str(price["usdBlue"]) + "\n"
        f.write(s)

