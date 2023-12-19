import axios from "axios";

export async function fetchJson(url: string): Promise<any> {
    const { data } = await axios.get(url);
    return data;
}
