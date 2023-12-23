export interface CardFooterDiffProps {
  title: string;
  diff: number;
}

export function CardFooterDiff(props: CardFooterDiffProps) {
  return (
    <div>
      <span className="text-sm text-gray-500 mr-1">{props.title}</span>
      {props.diff > 0 ? (
        <span className="text-sm text-green-600">+{props.diff}%</span>
      ) : (
        <span className="text-sm text-red-600">-{props.diff * -1}%</span>
      )}
    </div>
  );
}
