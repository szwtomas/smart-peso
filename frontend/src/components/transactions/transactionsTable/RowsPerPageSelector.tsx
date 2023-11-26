export interface RowsPerPageSelectorProps {
  onRowsPerPageChange: (e: React.ChangeEvent<HTMLSelectElement>) => void;
}

export function RowsPerPageSelector(props: RowsPerPageSelectorProps) {
  return (
    <label className="flex items-center text-default-400 text-small">
      Rows per page:
      <select
        className="bg-transparent outline-none text-default-400 text-small"
        onChange={props.onRowsPerPageChange}
      >
        <option value="5">5</option>
        <option value="10">10</option>
        <option value="15">15</option>
      </select>
    </label>
  );
}
