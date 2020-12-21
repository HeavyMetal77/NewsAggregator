package ua.tarastom.newsaggregator;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import ua.tarastom.newsaggregator.models.Article;
import ua.tarastom.newsaggregator.utils.Utils;

public class SourceAdapter extends RecyclerView.Adapter<SourceAdapter.SourceHolder> {

    private List<Article> articles;

    public SourceAdapter(List<Article> articles) {
        this.articles = articles;
    }

    @NonNull
    @Override
    public SourceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_source_list, parent, false);
        return new SourceHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SourceHolder holder, int position) {
        Article article = articles.get(position);
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.error(Utils.getRandomDrawbleColor());
        Glide.with(holder.itemView)
                .load(article.getLogo())
                .apply(requestOptions)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.imageViewLogo);
        holder.textViewTitle.setText(article.getTitleChannel());
        holder.textViewSource.setText(article.getSource());
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    static class SourceHolder extends RecyclerView.ViewHolder {

        private ImageView imageViewLogo;
        private TextView textViewSource;
        private TextView textViewTitle;

        public SourceHolder(@NonNull View itemView) {
            super(itemView);
            imageViewLogo = itemView.findViewById(R.id.imageViewLogo);
            textViewSource = itemView.findViewById(R.id.textViewSource);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
        }
    }
}
